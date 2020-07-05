package org.goodiemania.melinoe.framework.session;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.junit.FlowDecorator;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class InternalSessionImpl implements InternalSession {
    private final Logger logger;
    private final FlowDecorator flowDecorator;
    private final RawWebDriver rawWebDriver;


    public InternalSessionImpl(final MetaSession metaSession, final Logger logger) {
        this.logger = logger;

        this.rawWebDriver = new RawWebDriver(metaSession, this);
        this.flowDecorator = new FlowDecorator(this);
    }

    @Override
    public Session getSession() {
        return new SessionImpl(logger, rawWebDriver);
    }

    @Override
    public FlowDecorator getFlowDecorator() {
        return flowDecorator;
    }

    @Override
    public RawWebDriver getRawWebDriver() {
        return rawWebDriver;
    }
}
