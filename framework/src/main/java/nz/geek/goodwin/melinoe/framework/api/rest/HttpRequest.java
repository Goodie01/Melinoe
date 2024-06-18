package nz.geek.goodwin.melinoe.framework.api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Goodie
 */
public interface HttpRequest {
    HttpRequest header(String name, String value);

    HttpRequest queryString(String name, Object value);

    HttpRequest body(Object objectBody);

    HttpRequest body(String body);

    HttpResult<String> resultAsString();

    HttpResult<JsonNode> result();

    <T> HttpResult<T> result(Class<T> tClass);

    <T> HttpResult<T> result(TypeReference<T> typeReference);
}
