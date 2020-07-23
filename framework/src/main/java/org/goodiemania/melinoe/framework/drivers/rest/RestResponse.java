package org.goodiemania.melinoe.framework.drivers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.Arrays;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.rest.validators.RestValidator;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.junit.jupiter.api.Assertions;

public class RestResponse {
    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final String response;
    private final int statusCode;
    private final Exception exception;

    public RestResponse(final Logger logger, final ObjectMapper objectMapper, final HttpResponse<String> stringHttpResponse) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.response = stringHttpResponse.body();
        this.statusCode = stringHttpResponse.statusCode();
        this.exception = null;
    }

    public RestResponse(final Logger logger, final ObjectMapper objectMapper, final Exception e) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.exception = e;
        this.statusCode = -1;
        this.response = "";
    }

    public <T> T asObject(final Class<T> classz) {
        try {
            return this.objectMapper.readValue(response, classz);
        } catch (JsonProcessingException e) {
            throw new MelinoeException("Unable to convert object", e);
        }
    }

    public void validate(final RestValidator... restValidators) {
        logger.add()
                .withMessage("Validating rest response");

        Arrays.stream(restValidators)
                .map(restValidator -> restValidator.validate(this))
                .forEach(validationResult -> {
                    validationResult.getMessages().forEach(s -> {
                        if (validationResult.isValid()) {
                            logger.add()
                                    .withMessage(s);
                        } else {
                            logger.add()
                                    .withMessage(s)
                                    .fail();
                        }
                    });
                });

        if (!logger.getHasPassed()) {
            logger.add()
                    .withMessage("Failure in validation detected. Failing now.")
                    .fail();
            Assertions.fail();
        }
    }

    public String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Exception getException() {
        return exception;
    }
}
