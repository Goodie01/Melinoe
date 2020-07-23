package org.goodiemania.melinoe.framework.session;

import java.net.URI;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.rest.RestRequest;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.rest.RestRequestImpl;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class SessionImpl implements Session {
    private final Logger logger;
    private final MetaSession metaSession;
    private final ClassLogger classLogger;
    private final FlowDecorator flowDecorator;
    private final RawWebDriver rawWebDriver;
    private final HttpRequestExecutor requestExecutor;

    public SessionImpl(final MetaSession metaSession,
                       final ClassLogger classLogger,
                       final Logger logger,
                       final RawWebDriver rawWebDriver,
                       final HttpRequestExecutor httpRequestExecutor) {
        this.metaSession = metaSession;
        this.classLogger = classLogger;
        this.rawWebDriver = rawWebDriver;
        this.logger = logger;
        this.requestExecutor = httpRequestExecutor;

        this.flowDecorator = new FlowDecorator(logger, rawWebDriver, this);
    }


    @Override
    public RestRequest rest(final String uri) {
        return new RestRequestImpl(requestExecutor, uri);
    }

    @Override
    public RestRequest rest(final URI uri) {
        return new RestRequestImpl(requestExecutor, uri);
    }

    @Override
    public WebDriver web() {
        return rawWebDriver.getWebDriver();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void decorate(final Object flow) {
        flowDecorator.decorate(flow);
    }

    @Override
    public Session createSubSession(final String name) {
        final Logger subSessionLogger = classLogger.createSubSessionLogger(logger.getMethodName());

        logger.add()
                .withMessage(name)
                .withSubSessionLogger(subSessionLogger);

        final RawWebDriver rawWebDriver = new RawWebDriver(metaSession, subSessionLogger);

        return new SessionImpl(metaSession,
                classLogger,
                subSessionLogger,
                rawWebDriver,
                requestExecutor);
    }
}
