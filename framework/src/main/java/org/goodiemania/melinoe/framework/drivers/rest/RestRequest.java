package org.goodiemania.melinoe.framework.drivers.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.goodiemania.melinoe.framework.api.HttpMethodType;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;

public class RestRequest {
    private final HttpRequestExecutor requestExecutor;
    private final URI uri;
    private final HttpMethodType httpMethodType;
    private final String body;
    private final Map<String, List<String>> headers;

    public RestRequest(final HttpRequestExecutor requestExecutor, final URI uri) {
        this.requestExecutor = requestExecutor;
        this.uri = uri;
        this.httpMethodType = HttpMethodType.GET;
        this.body = "";
        this.headers = Collections.emptyMap();
    }

    public RestRequest(final HttpRequestExecutor requestExecutor, final String uri) {
        try {
            this.uri = URI.create(uri);
        } catch (IllegalArgumentException e) {
            throw new MelinoeException("Malformed URI", e);
        }
        this.requestExecutor = requestExecutor;
        this.httpMethodType = HttpMethodType.GET;
        this.body = "";
        this.headers = Collections.emptyMap();
    }

    private RestRequest(final HttpRequestExecutor requestExecutor,
                        final URI uri,
                        final HttpMethodType httpMethodType,
                        final String body,
                        final Map<String, List<String>> headers) {
        this.requestExecutor = requestExecutor;
        this.uri = uri;
        this.httpMethodType = httpMethodType;
        this.body = body;
        this.headers = headers;
    }

    public RestRequest withUri(final String uriString) {
        try {
            return withUri(URI.create(uriString));
        } catch (IllegalArgumentException e) {
            throw new MelinoeException("Malformed URI", e);
        }
    }

    public RestRequest withUri(final URI uri) {
        return new RestRequest(requestExecutor, uri, httpMethodType, body, headers);
    }

    public RestRequest withHttpMethodType(final HttpMethodType httpMethodType) {
        return new RestRequest(requestExecutor, uri, httpMethodType, body, headers);
    }

    public RestRequest withBody(final String body) {
        return new RestRequest(requestExecutor, uri, httpMethodType, body, headers);
    }

    public RestRequest withHeader(final String key, final String value) {
        //this ensures that even if someone were to get a reference to this.headers they can't modify it
        //this means that this entire object is immutable, yay!
        HashMap<String, List<String>> headers = new HashMap<>(this.headers);
        ArrayList<String> headerValues = new ArrayList<>(headers.getOrDefault(key, Collections.emptyList()));
        headerValues.add(value);

        headers.put(key, Collections.unmodifiableList(headerValues));

        return new RestRequest(requestExecutor, uri, httpMethodType, body, Collections.unmodifiableMap(headers));
    }

    public RestResponse execute() {
        return requestExecutor.execute(uri, httpMethodType, body, headers);
    }
}
