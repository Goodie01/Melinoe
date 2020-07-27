package org.goodiemania.melinoe.framework.session;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class InternalSessionImpl implements InternalSession {
    private final Logger logger;
    private final RawWebDriver rawWebDriver;
    private final HttpRequestExecutor httpRequestExecutor;
    private final MetaSession metaSession;
    private final ClassLogger classLogger;

    public InternalSessionImpl(final MetaSession metaSession,
                               final ClassLogger classLogger,
                               final Logger logger) {
        this.metaSession = metaSession;
        this.classLogger = classLogger;
        this.logger = logger;

        this.rawWebDriver = new RawWebDriver(this);
        this.httpRequestExecutor = new HttpRequestExecutor(this);
    }

    @Override
    public MetaSession getMetaSession() {
        return metaSession;
    }

    @Override
    public Session getSession() {
        return new SessionImpl(this);
    }

    @Override
    public ClassLogger getClassLogger() {
        return classLogger;
    }

    @Override
    public RawWebDriver getRawWebDriver() {
        return rawWebDriver;
    }

    @Override
    public HttpRequestExecutor getHttpRequestExecutor() {
        return httpRequestExecutor;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
