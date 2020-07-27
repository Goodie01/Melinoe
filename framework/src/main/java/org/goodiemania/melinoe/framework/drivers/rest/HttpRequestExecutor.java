package org.goodiemania.melinoe.framework.drivers.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.goodiemania.melinoe.framework.api.HttpMethodType;
import org.goodiemania.melinoe.framework.session.InternalSession;

public class HttpRequestExecutor {
    private final InternalSession session;


    public HttpRequestExecutor(final InternalSession session) {
        this.session = session;
    }

    public RestResponse execute(final URI uri, final HttpMethodType httpMethodType, final String body, final Map<String, List<String>> headers) {
        String httpMethod = httpMethodType.toString();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .method(httpMethod, HttpRequest.BodyPublishers.ofString(body));

        headers.forEach((key, valueList) -> valueList.forEach(value -> builder.header(key, value)));

        try {
            long startTime = System.currentTimeMillis();
            HttpResponse<String> stringHttpResponse = session.getMetaSession().getHttpClient().send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString());
            long stopTime = System.currentTimeMillis();

            session.getSession().getLogger().add()
                    .withMessage(String.format("Made a HTTP request to %s with a %d status in %dms",
                            uri,
                            stringHttpResponse.statusCode(),
                            stopTime - startTime))
                    .withSecondMessage("Click for raw response")
                    .withHiddenInfo(stringHttpResponse.body());


            return new RestResponse(session.getSession().getLogger(), session.getMetaSession().getObjectMapper(), stringHttpResponse);
        } catch (IOException e) {
            return new RestResponse(session.getSession().getLogger(), session.getMetaSession().getObjectMapper(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new RestResponse(session.getSession().getLogger(), session.getMetaSession().getObjectMapper(), e);
        }
    }
}
