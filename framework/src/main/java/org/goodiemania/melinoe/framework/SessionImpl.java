package org.goodiemania.melinoe.framework;

import org.goodiemania.melinoe.framework.session.MetaSession;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.web.RawWebDriver;
import org.goodiemania.melinoe.framework.web.WebDriver;
import org.goodiemania.melinoe.framework.web.WebDriverImpl;

public class SessionImpl implements Session {
    private final MetaSession metaSession;
    private final ClassLogger classLogger;

    private RawWebDriver rawWebDriver;

    public SessionImpl(final MetaSession metaSession, final ClassLogger classLogger, final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
        this.classLogger = classLogger;
        this.metaSession = metaSession;
    }

    @Override
    public void rest() {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public WebDriver web() {
        WebDriverImpl webDriver = rawWebDriver.getWebDriver();
        metaSession.addDriver(webDriver);
        return webDriver;
    }

    @Override
    public ClassLogger getLogger() {
        return classLogger;
    }
}
