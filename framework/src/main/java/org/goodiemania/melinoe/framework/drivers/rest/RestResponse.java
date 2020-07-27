package org.goodiemania.melinoe.framework.drivers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.rest.validators.RestValidator;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.validators.Validator;

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
            return this.internalSession.getMetaSession().getObjectMapper().readValue(response, classz);
        } catch (JsonProcessingException e) {
            throw new MelinoeException("Unable to convert object", e);
        }
    }

    public void validate(final List<RestValidator> restValidators) {
        internalSession.getSession().getLogger().add().withMessage("Validating rest response");

        final List<Validator> validators = restValidators.stream()
                .map(restValidator -> (Validator) () -> restValidator.validate(this))
                .collect(Collectors.toList());

        internalSession.getSession().verify(validators);
    }

    public void validate(final RestValidator... restValidators) {
        validate(Arrays.asList(restValidators));
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
