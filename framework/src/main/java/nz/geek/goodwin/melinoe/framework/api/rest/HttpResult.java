package nz.geek.goodwin.melinoe.framework.api.rest;

import nz.geek.goodwin.melinoe.framework.api.rest.validation.RestValidator;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Goodie
 */
public interface HttpResult<T> {

    URI reqUri();

    String reqMethod();

    Map<String, List<String>> reqHeaders();

    String reqBody();

    URI respUri();

    int respStatus();

    Map<String, List<String>> respHeaders();

    T respBody();

    default HttpResult<T> verify(final RestValidator<T>... validators) {
        return verify(Arrays.asList(validators));
    }

    HttpResult<T> verify(List<RestValidator<T>> validators);
}
