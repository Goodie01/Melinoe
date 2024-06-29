package nz.geek.goodwin.melinoe.framework.internal.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.log.LogMessage;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.rest.HttpRequest;
import nz.geek.goodwin.melinoe.framework.api.rest.HttpResult;
import nz.geek.goodwin.melinoe.framework.api.rest.validation.RestValidator;
import nz.geek.goodwin.melinoe.framework.internal.verify.VerificationUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;

import static java.net.URI.create;

/**
 * @author Goodie
 */
public class HttpRequestImpl implements HttpRequest {
    private final String method;
    private final Map<String, List<String>> headers = new HashMap<>();
    private String url;
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final Logger logger;
    private String body;

    public HttpRequestImpl(String method, String url, HttpClient client, ObjectMapper objectMapper, Logger logger) {
        this.method = String.valueOf(method).toUpperCase();
        this.url = url;
        this.client = client;
        this.objectMapper = objectMapper;
        this.logger = logger;
    }

    @Override
    public HttpRequest header(final String name, final String value) {
        headers.computeIfAbsent(name, k -> new ArrayList<>(1))
                .add(value);
        return this;
    }

    @Override
    public HttpRequest queryString(String name, Object value) {
        StringBuilder queryString = new StringBuilder();
        if (url.contains("?")) {
            queryString.append("&");
        } else {
            queryString.append("?");
        }
        try {
            queryString.append(URLEncoder.encode(name, "UTF-8"));
            if (value != null) {
                queryString.append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new MelinoeException(e);
        }
        url += queryString.toString();

        return this;
    }

    @Override
    public HttpRequest body(Object objectBody) {
        if (objectBody instanceof String) {
            return body((String) objectBody);
        }

        try {
            return body(objectMapper.writeValueAsString(objectBody));
        } catch (JsonProcessingException e) {
            throw new MelinoeException(e);
        }
    }

    @Override
    public HttpRequest body(String body) {
        this.body = body;
        return this;
    }

    @Override
    public HttpResult<String> verifyAsString(List<RestValidator<String>> validators) {
        return gettHttpResult(s -> s, validators);
    }

    @Override
    public HttpResult<JsonNode> verify(List<RestValidator<JsonNode>> validators) {
        return gettHttpResult(objectMapper::readTree, validators);
    }

    @Override
    public <T> HttpResult<T> verify(TypeReference<T> typeReference, List<RestValidator<T>> validators) {
        return gettHttpResult(s -> objectMapper.readValue(s, typeReference), validators);
    }

    @Override
    public <T> HttpResult<T> verify(Class<T> tClass, final List<RestValidator<T>> validators) {
        return gettHttpResult(s -> objectMapper.readValue(s, tClass), validators);
    }

    private <T> HttpResult<T> gettHttpResult(Function<String, T> tSupplier, List<RestValidator<T>> validators) {
        java.net.http.HttpRequest req = buildHttpRequest();
        Logger requestSublogger = logger.createSublogger(req.method() + " " + req.uri().getHost() + req.uri().getPath());

        return VerificationUtils.validate(validators, requestSublogger, () -> {
            try {
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                log(requestSublogger, req, resp);

                T t = tSupplier.apply(resp.body());

                return new HttpResultImpl<>(req, resp, t, logger);
            } catch (Exception e) {
                throw new MelinoeException(e);
            }
        });
    }

    private String reqBody(java.net.http.HttpRequest req) {
        return req.bodyPublisher().map(p -> {
            var bodySubscriber = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
            var flowSubscriber = new StringSubscriber(bodySubscriber);
            p.subscribe(flowSubscriber);
            return bodySubscriber.getBody().toCompletableFuture().join();
        }).orElseThrow();
    }

    private void log(Logger logger, java.net.http.HttpRequest req, HttpResponse<String> resp) {
        logger.add().withMessage("Making request");
        LogMessage request = logger.add()
                .withMessage("> " + req.method() + " " + req.uri())
                .withMessage("> " + req.version().map(Enum::toString).orElse("Unspecified version"));

        req.headers().map().forEach((name, values) -> values.forEach(value -> {
            request.withMessage("> " + name + ": " + value);
        }));

        String jsonReq = reqBody(req);
        if (StringUtils.isNotBlank(jsonReq)) {
            logger.add().withMessage(Prettifier.prettify(jsonReq));
        }

        LogMessage response = logger.add()
                .withMessage("< " + resp.version().toString() + " " + resp.statusCode())
                .withMessage("< " + resp.uri());

        resp.headers().map().forEach((name, values) -> values.forEach(value -> {
            response.withMessage("< " + name + ": " + value);
        }));

        String jsonResp = StringUtils.trim(resp.body());//TODO handle bodies other than strings better

        if (StringUtils.startsWith(jsonResp, "{")) {
            logger.add().withMessage(Prettifier.prettify(jsonResp));
        } else {
            logger.add().withMessage(jsonResp);
        }
    }

    private java.net.http.HttpRequest buildHttpRequest() {
        URI requestUrl = create(url);
        java.net.http.HttpRequest.BodyPublisher bodyPublisher;
        if (body == null) {
            bodyPublisher = java.net.http.HttpRequest.BodyPublishers.noBody();
        } else {
            bodyPublisher = java.net.http.HttpRequest.BodyPublishers.ofString(body);
        }

        java.net.http.HttpRequest.Builder req = java.net.http.HttpRequest.newBuilder()
                .method(method, bodyPublisher)
                .uri(requestUrl);

        headers.forEach((s, strings) -> strings.forEach(s1 -> {
            req.header(s, s1);
        }));

        return req.build();
    }

    private static final class StringSubscriber implements Flow.Subscriber<ByteBuffer> {
        final HttpResponse.BodySubscriber<String> wrapped;

        StringSubscriber(HttpResponse.BodySubscriber<String> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            wrapped.onSubscribe(subscription);
        }

        @Override
        public void onNext(ByteBuffer item) {
            wrapped.onNext(List.of(item));
        }

        @Override
        public void onError(Throwable throwable) {
            wrapped.onError(throwable);
        }

        @Override
        public void onComplete() {
            wrapped.onComplete();
        }
    }
    public interface Function<T, R> {

        R apply(T t) throws Exception;
    }
}
