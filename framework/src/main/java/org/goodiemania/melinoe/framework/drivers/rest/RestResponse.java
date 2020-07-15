package org.goodiemania.melinoe.framework.drivers.rest;

import java.net.http.HttpResponse;

public class RestResponse {
    private final String response;
    private final int statusCode;
    private final Exception exception;

    public RestResponse(final HttpResponse<String> stringHttpResponse) {
        this.response = stringHttpResponse.body();
        this.statusCode = stringHttpResponse.statusCode();
        this.exception = null;
    }

    public RestResponse(final Exception e) {
        this.exception = e;
        this.statusCode = -1;
        this.response = "";
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
