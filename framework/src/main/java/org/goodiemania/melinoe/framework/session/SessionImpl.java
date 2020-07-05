package org.goodiemania.melinoe.framework.session;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.drivers.web.WebDriver;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class SessionImpl implements Session {
    private final Logger logger;

    private RawWebDriver rawWebDriver;

    public SessionImpl(final Logger logger, final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
        this.logger = logger;
    }

    @Override
    public void rest() {
        throw new IllegalStateException("Not yet implemented");
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
