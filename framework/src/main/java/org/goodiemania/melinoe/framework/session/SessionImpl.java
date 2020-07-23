package org.goodiemania.melinoe.framework.session;

import java.net.URI;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.rest.RestRequest;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.rest.RestRequestImpl;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class SessionImpl implements Session {
    private final InternalSession internalSession;
    private final RawWebDriver rawWebDriver;
    private final HttpRequestExecutor httpRequestExecutor;
    private final FlowDecorator flowDecorator;
    private final Logger logger;

    public SessionImpl(final InternalSession internalSession,
                       final Logger logger,
                       final RawWebDriver rawWebDriver,
                       final HttpRequestExecutor httpRequestExecutor,
                       final FlowDecorator flowDecorator) {
        this.internalSession = internalSession;
        this.logger = logger;
        this.rawWebDriver = rawWebDriver;
        this.httpRequestExecutor = httpRequestExecutor;
        this.flowDecorator = flowDecorator;
    }


    @Override
    public RestRequest rest(final String uri) {
        return new RestRequestImpl(httpRequestExecutor, uri);
    }

    @Override
    public RestRequest rest(final URI uri) {
        return new RestRequestImpl(httpRequestExecutor, uri);
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
        final Logger subSessionLogger = internalSession.getClassLogger().createSubSessionLogger(logger.getMethodName());

        logger.add()
                .withMessage(name)
                .withSubSessionLogger(subSessionLogger);

        RawWebDriver rawWebDriver = new RawWebDriver(internalSession.getMetaSession(), logger, "sessionImpl");
        HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor(internalSession);
        FlowDecorator flowDecorator = new FlowDecorator(this, rawWebDriver);

        return new SessionImpl(internalSession,
                subSessionLogger,
                rawWebDriver,
                httpRequestExecutor,
                flowDecorator);
    }
}
