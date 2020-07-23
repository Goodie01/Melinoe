package org.goodiemania.melinoe.framework.drivers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.goodiemania.melinoe.framework.api.HttpMethodType;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class HttpRequestExecutor {
    private final HttpClient httpClient;
    private final Logger logger;
    private final ObjectMapper objectMapper;

    public HttpRequestExecutor(final InternalSession session) {
        this.logger = session.getSession().getLogger();
        this.objectMapper = session.getObjectMapper();
        this.httpClient = session.getHttpClient();
    }

    public RestResponse execute(final URI uri, final HttpMethodType httpMethodType, final String body, final Map<String, List<String>> headers) {
        String httpMethod = httpMethodType.toString();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .method(httpMethod, HttpRequest.BodyPublishers.ofString(body));

        headers.forEach((key, valueList) -> valueList.forEach(value -> builder.header(key, value)));

        try {
            long startTime = System.currentTimeMillis();
            HttpResponse<String> stringHttpResponse = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            long stopTime = System.currentTimeMillis();

            logger.add()
                    .withMessage(String.format("Made a HTTP request to %s with a %d status in %dms",
                            uri,
                            stringHttpResponse.statusCode(),
                            stopTime - startTime))
                    .withSecondMessage("Click for raw response")
                    .withHiddenInfo(stringHttpResponse.body());


            return new RestResponse(logger, objectMapper, stringHttpResponse);
        } catch (IOException e) {
            return new RestResponse(logger, objectMapper, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new RestResponse(logger, objectMapper, e);
        }
    }
}
