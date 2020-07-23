package org.goodiemania.melinoe.framework.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class InternalSessionImpl implements InternalSession {
    private final InternalSession internalSession;
    private final FlowDecorator flowDecorator;
    private final Logger logger;
    private final RawWebDriver rawWebDriver;
    private final HttpRequestExecutor httpRequestExecutor;

    public InternalSessionImpl(final InternalSession internalSession,
                               final RawWebDriver rawWebDriver,
                               final HttpRequestExecutor httpRequestExecutor,
                               final FlowDecorator flowDecorator,
                               final Logger logger) {

        this.internalSession = internalSession;
        this.rawWebDriver = rawWebDriver;
        this.httpRequestExecutor = httpRequestExecutor;
        this.flowDecorator = flowDecorator;
        this.logger = logger;
    }

    @Override
    public MetaSession getMetaSession() {
        return internalSession.getMetaSession();
    }

    @Override
    public Session getSession() {
        return new SessionImpl(this, logger, rawWebDriver, httpRequestExecutor, flowDecorator);
    }

    @Override
    public FlowDecorator getFlowDecorator() {
        return flowDecorator;
    }

    @Override
    public RawWebDriver getRawWebDriver() {
        return rawWebDriver;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return internalSession.getObjectMapper();
    }

    @Override
    public HttpClient getHttpClient() {
        return internalSession.getHttpClient();
    }

    @Override
    public ClassLogger getClassLogger() {
        return internalSession.getClassLogger();
    }
}
