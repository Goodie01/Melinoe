package nz.geek.goodwin.melinoe.framework.api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import nz.geek.goodwin.melinoe.framework.api.rest.validation.RestValidator;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @author Goodie
 */
public interface HttpRequest {
    HttpRequest header(String name, String value);

    HttpRequest queryString(String name, Object value);

    HttpRequest body(Object objectBody);

    HttpRequest body(String body);
    HttpResult<String> verifyAsString(List<RestValidator<String>> validators);
    HttpResult<JsonNode> verify(List<RestValidator<JsonNode>> validators);
    <T> HttpResult<T> verify(TypeReference<T> tClass, List<RestValidator<T>> validators);
    <T> HttpResult<T> verify(Class<T> tClass, List<RestValidator<T>> validators);

    default HttpResult<String> verifyAsString(RestValidator<String>... validators) {
        return verifyAsString(List.of(validators));
    }
    default HttpResult<JsonNode> verify(RestValidator<JsonNode>... validators) {
        return verify(List.of(validators));
    }
    default <T> HttpResult<T> verify(TypeReference<T> tClass, RestValidator<T>... validators) {
        return verify(tClass, List.of(validators));
    }
    default <T> HttpResult<T> verify(Class<T> tClass, RestValidator<T>... validators) {
        return verify(tClass, List.of(validators));
    }
}
