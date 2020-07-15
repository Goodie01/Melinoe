package org.goodiemania.melinoe.framework.session;

import java.net.URI;
import java.net.http.HttpClient;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.rest.RestRequest;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.drivers.web.WebDriver;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class SessionImpl implements Session {
    private final Logger logger;
    private final RawWebDriver rawWebDriver;
    private final HttpRequestExecutor requestExecutor;

    public SessionImpl(final Logger logger, final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
        this.requestExecutor = new HttpRequestExecutor(this, HttpClient.newBuilder().build());
        this.logger = logger;
    }


    @Override
    public RestRequest rest(final String uri) {
        return new RestRequest(requestExecutor, uri);
    }

    @Override
    public RestRequest rest(final URI uri) {
        return new RestRequest(requestExecutor, uri);
    }

    @Override
    public WebDriver web() {
        return rawWebDriver.getWebDriver();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
