package org.goodiemania.melinoe.framework;

import org.goodiemania.melinoe.framework.session.MetaSession;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.web.RawWebDriver;

public class InternalSession {
    private final MetaSession metaSession;
    private final ClassLogger classLogger;
    private final FlowDecorator flowDecorator;
    private final RawWebDriver rawWebDriver;


    public InternalSession(final MetaSession metaSession, final ClassLogger classLogger) {
        this.metaSession = metaSession;
        this.classLogger = classLogger;

        this.rawWebDriver = new RawWebDriver(this);
        this.flowDecorator = new FlowDecorator(rawWebDriver);
    }

    public Session getSession() {
        return new SessionImpl(metaSession, classLogger, rawWebDriver);
    }

    public FlowDecorator getFlowDecorator() {
        return flowDecorator;
    }

    public RawWebDriver getRawWebDriver() {
        return rawWebDriver;
    }
}
