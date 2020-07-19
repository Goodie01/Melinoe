package org.goodiemania.melinoe.framework.drivers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.rest.validators.RestValidator;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.junit.jupiter.api.Assertions;

public class RestResponse {
    private final InternalSession internalSession;
    private final String response;
    private final int statusCode;
    private final Exception exception;

    public RestResponse(final InternalSession internalSession, final HttpResponse<String> stringHttpResponse) {
        this.internalSession = internalSession;
        this.response = stringHttpResponse.body();
        this.statusCode = stringHttpResponse.statusCode();
        this.exception = null;
    }

    public RestResponse(final InternalSession internalSession, final Exception e) {
        this.internalSession = internalSession;
        this.exception = e;
        this.statusCode = -1;
        this.response = "";
    }

    public <T> T asObject(final Class<T> classz) {
        try {
            return this.internalSession.getObjectMapper().readValue(response, classz);
        } catch (JsonProcessingException e) {
            throw new MelinoeException("Unable to convert object", e);
        }
    }

    public void validate(final RestValidator... restValidators) {
        Session session = internalSession.getSession();
        session.getLogger().add()
                .withMessage("Validating rest response");

        Arrays.stream(restValidators)
                .map(restValidator -> restValidator.validate(session, this))
                .forEach(validationResult -> {
                    validationResult.getMessages().forEach(s -> {
                        if (validationResult.isValid()) {
                            session.getLogger().add()
                                    .withMessage(s);
                        } else {
                            session.getLogger().add()
                                    .withMessage(s)
                                    .fail();
                        }
                    });
                });

        if (!session.getLogger().getHasPassed()) {
            session.getLogger().add()
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
