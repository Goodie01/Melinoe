package org.goodiemania.melinoe.framework.drivers.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.goodiemania.melinoe.framework.api.HttpMethodType;
import org.goodiemania.melinoe.framework.api.Session;

public class HttpRequestExecutor {
    private final Session session;
    private final HttpClient httpClient;

    public HttpRequestExecutor(final Session session, final HttpClient httpClient) {
        this.session = session;
        this.httpClient = httpClient;
    }

    public RestResponse execute(final URI uri, final HttpMethodType httpMethodType, final String body, final Map<String, List<String>> headers) {
        String httpMethod = httpMethodType.toString();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .method(httpMethod, HttpRequest.BodyPublishers.noBody());

        headers.forEach((key, valueList) -> valueList.forEach(value -> builder.header(key, value)));

        try {
            long startTime = System.currentTimeMillis();
            HttpResponse<String> stringHttpResponse = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            long stopTime = System.currentTimeMillis();

            session.getLogger().add()
                    .withMessage(String.format("Made a HTTP request to %s with a %d status in %dms",
                            uri,
                            stringHttpResponse.statusCode(),
                            stopTime - startTime))
                    .withHiddenInfo(stringHttpResponse.body());

            return new RestResponse(stringHttpResponse);
        } catch (IOException | InterruptedException e) {
            return new RestResponse(e);
        }
    }
}
