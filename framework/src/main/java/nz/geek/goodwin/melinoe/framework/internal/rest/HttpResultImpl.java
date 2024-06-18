package nz.geek.goodwin.melinoe.framework.internal.rest;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.log.LogMessage;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.rest.HttpResult;
import nz.geek.goodwin.melinoe.framework.api.rest.validation.RestValidator;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;
import nz.geek.goodwin.melinoe.framework.internal.verify.VerificationUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

/**
 * @author Goodie
 */
public class HttpResultImpl<T> implements HttpResult<T> {
    private final Logger logger;
    private final HttpRequest req;
    private final HttpResponse<T> resp;
    private boolean checked = false;

    public HttpResultImpl(HttpRequest req, HttpResponse<T> resp, Logger logger) {
        this.req = req;
        this.resp = resp;
        this.logger = logger;

        Logger requestSublogger = logger.createSublogger(reqMethod() + " " + reqUri().getHost() + reqUri().getPath());
        LogMessage request = requestSublogger.add()
                .withMessage("> " + reqMethod() + " " + reqUri())
                .withMessage("> " + req.version().map(Enum::toString).orElse("Unspecified version"));

        reqHeaders().forEach((name, values) -> values.forEach(value -> {
            request.withMessage("> " + name + ": " + value);
        }));

        String json = reqBody();
        if(StringUtils.isNotBlank(json)) {
            requestSublogger.add().withMessage(Prettifier.prettify(json));
        }

        LogMessage response = requestSublogger.add()
                .withMessage("< " + resp.version().toString() + " " + respStatus())
                .withMessage("< " + respUri());

        respHeaders().forEach((name, values) -> values.forEach(value -> {
            response.withMessage("< " + name + ": " + value);
        }));

        json = String.valueOf(getBody());//TODO handle bodies other than strings

        if(StringUtils.isNotBlank(json)) {
            requestSublogger.add().withMessage(Prettifier.prettify(json));
        }
    }

    @Override
    public URI reqUri() {
        return req.uri();
    }

    @Override
    public String reqMethod() {
        return req.method();
    }

    @Override
    public Map<String, List<String>> respHeaders() {
        return this.resp.headers().map();
    }

    @Override
    public URI respUri() {
        return this.resp.uri();
    }

    @Override
    public int respStatus() {
        return this.resp.statusCode();
    }

    @Override
    public Map<String, List<String>> reqHeaders() {
        return this.req.headers().map();
    }

    @Override
    public T respBody() {
        if (!checked) {
            throw new MelinoeException("Please check run at least one validator before interacting with a Http Result.");
        }
        return getBody();
    }

    private T getBody() {
        return this.resp.body();
    }

    @Override
    public HttpResult<T> verify(List<RestValidator<T>> validators) {
        Logger verifyingLogger;
        if (!checked) {
            verifyingLogger = logger.createSublogger("Verifying response");
        } else {
            verifyingLogger = logger;
        }

        checked = true;
        List<ValidationResult> list = validators.stream()
                .map(webValidator -> webValidator.validate(this))
                .toList();

        list.forEach(validationResult -> {
            String messages = validationResult.messages().stream().collect(Collectors.joining(System.lineSeparator()));
            verifyingLogger.add().withMessage(messages).withSuccess(validationResult.valid());
        });

        VerificationUtils.checkVerificationResults(list);

        return this;
    }


    @Override
    public String reqBody() {
        return req.bodyPublisher().map(p -> {
            var bodySubscriber = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
            var flowSubscriber = new HttpResultImpl.StringSubscriber(bodySubscriber);
            p.subscribe(flowSubscriber);
            return bodySubscriber.getBody().toCompletableFuture().join();
        }).orElseThrow();
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
}
