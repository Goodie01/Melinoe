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
    private final HttpResponse<String> resp;
    private final T body;

    public HttpResultImpl(HttpRequest req, HttpResponse<String> resp, T t, Logger logger) {
        this.req = req;
        this.resp = resp;
        this.body = t;
        this.logger = logger;
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
    public Map<String, List<String>> reqHeaders() {
        return this.req.headers().map();
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

    @Override
    public URI respUri() {
        return this.resp.uri();
    }

    @Override
    public int respStatus() {
        return this.resp.statusCode();
    }

    @Override
    public Map<String, List<String>> respHeaders() {
        return this.resp.headers().map();
    }

    @Override
    public T respBody() {
        return body;
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
