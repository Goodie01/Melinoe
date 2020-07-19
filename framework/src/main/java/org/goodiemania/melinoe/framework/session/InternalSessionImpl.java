package org.goodiemania.melinoe.framework.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class InternalSessionImpl implements InternalSession {
    private final Logger logger;
    private final FlowDecorator flowDecorator;
    private final RawWebDriver rawWebDriver;
    private final ObjectMapper objectMapper;
    private final HttpRequestExecutor httpRequestExecutor;


    public InternalSessionImpl(final MetaSession metaSession, final Logger logger, final ObjectMapper objectMapper) {
        this.logger = logger;
        this.objectMapper = objectMapper;

        this.rawWebDriver = new RawWebDriver(metaSession, this);
        this.flowDecorator = new FlowDecorator(this);
        this.httpRequestExecutor = new HttpRequestExecutor(this, HttpClient.newBuilder().build());
    }

    @Override
    public Session getSession() {
        return new SessionImpl(logger, rawWebDriver, httpRequestExecutor);
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
        return objectMapper;
    }
}
