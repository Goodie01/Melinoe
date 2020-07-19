package org.goodiemania.melinoe.framework.api.rest;

import java.net.URI;
import org.goodiemania.melinoe.framework.api.HttpMethodType;
import org.goodiemania.melinoe.framework.drivers.rest.RestResponse;

public interface RestRequest {
    RestRequest withUri(String uriString);

    RestRequest withUri(URI uri);

    RestRequest withHttpMethodType(HttpMethodType httpMethodType);

    RestRequest withBody(String body);

    RestRequest withHeader(String key, String value);

    RestResponse execute();
}
