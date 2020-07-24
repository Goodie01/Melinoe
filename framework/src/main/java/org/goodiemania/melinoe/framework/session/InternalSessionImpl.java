package org.goodiemania.melinoe.framework.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class InternalSessionImpl implements InternalSession {
    private final Logger logger;
    private final RawWebDriver rawWebDriver;
    private final HttpRequestExecutor httpRequestExecutor;
    private final ObjectMapper objectMapper;
    private final MetaSession metaSession;
    private final HttpClient httpClient;
    private final ClassLogger classLogger;

    public InternalSessionImpl(final MetaSession metaSession,
                               final ObjectMapper objectMapper,
                               final HttpClient httpClient,
                               final ClassLogger classLogger,
                               final RawWebDriver rawWebDriver,
                               final HttpRequestExecutor httpRequestExecutor,
                               final Logger logger) {
        this.metaSession = metaSession;
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.classLogger = classLogger;
        this.rawWebDriver = rawWebDriver;
        this.httpRequestExecutor = httpRequestExecutor;
        this.logger = logger;
    }

    @Override
    public MetaSession getMetaSession() {
        return metaSession;
    }

    @Override
    public Session getSession() {
        return new SessionImpl(this, logger, rawWebDriver, httpRequestExecutor);
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public ClassLogger getClassLogger() {
        return classLogger;
    }
}
