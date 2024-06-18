package nz.geek.goodwin.melinoe.framework.internal.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.rest.HttpRequest;
import nz.geek.goodwin.melinoe.framework.api.rest.HttpResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public HttpResult<String> resultAsString() {
        try {
            java.net.http.HttpRequest req = buildHttpRequest();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            return new HttpResultImpl<>(req, resp, logger);
        } catch (Exception e) {
            throw new MelinoeException(e);
        }
    }

    @Override
    public HttpResult<JsonNode> result() {
        try {
            java.net.http.HttpRequest req = buildHttpRequest();
            HttpResponse<JsonNode> resp = client.send(req, new JsonBodyHandler<>(objectMapper::readTree));

            return new HttpResultImpl<>(req, resp, logger);
        } catch (Exception e) {
            throw new MelinoeException(e);
        }
    }

    @Override
    public <T> HttpResult<T> result(Class<T> tClass) {
        try {
            java.net.http.HttpRequest req = buildHttpRequest();
            HttpResponse<T> resp = client.send(req, new JsonBodyHandler<>(inputStream -> objectMapper.readValue(inputStream, tClass)));

            return new HttpResultImpl<>(req, resp, logger);
        } catch (Exception e) {
            throw new MelinoeException(e);
        }
    }

    @Override
    public <T> HttpResult<T> result(TypeReference<T> typeReference) {
        try {
            java.net.http.HttpRequest req = buildHttpRequest();
            HttpResponse<T> resp = client.send(req, new JsonBodyHandler<>(inputStream -> objectMapper.readValue(inputStream, typeReference)));
            return new HttpResultImpl<T>(req, resp, logger);
        } catch (Exception e) {
            throw new MelinoeException(e);
        }
    }

    private java.net.http.HttpRequest buildHttpRequest() {
        URI requestUrl = create(url);
        java.net.http.HttpRequest.BodyPublisher bodyPublisher;
        if(body == null) {
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

    //https://stackoverflow.com/questions/57629401/deserializing-json-using-java-11-httpclient-and-custom-bodyhandler-with-jackson
    private class JsonBodyHandler<W> implements HttpResponse.BodyHandler<W> {

        private final Function<W> function;

        public JsonBodyHandler(Function<W> function) {
            this.function = function;
        }

        @Override
        public HttpResponse.BodySubscriber<W> apply(HttpResponse.ResponseInfo responseInfo) {
            return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.ofInputStream(),
                    inputStream -> {
                        try (InputStream stream = inputStream) {
                            return function.apply(stream);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }

    }

    private interface Function<R> {

        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         */
        R apply(InputStream t) throws IOException;
    }
}
