package org.goodiemania.melinoe.framework.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;

public class InternalSessionClassImpl implements InternalSession {
    private final MetaSession metaSession;
    private final ClassLogger classLogger;
    private final FlowDecorator flowDecorator;
    private final RawWebDriver rawWebDriver;
    private final ObjectMapper objectMapper;
    private final HttpRequestExecutor httpRequestExecutor;

    private Logger logger;
    private String classLoggerMethodName = "beforeAll";
    private String classLoggerDisplayName = "Before all";


    public InternalSessionClassImpl(final MetaSession metaSession, final ClassLogger classLogger) {
        this.metaSession = metaSession;
        this.classLogger = classLogger;

        this.logger = classLogger.createClassLogger(classLoggerMethodName, classLoggerDisplayName);

        this.rawWebDriver = new RawWebDriver(metaSession, logger);
        this.flowDecorator = new FlowDecorator(logger, rawWebDriver, getSession());
        this.objectMapper = new ObjectMapper();
        this.httpRequestExecutor = new HttpRequestExecutor(this, HttpClient.newBuilder().build());
    }

    @Override
    public Session getSession() {
        if (this.logger == null) {
            this.logger = classLogger.createClassLogger(classLoggerMethodName, classLoggerDisplayName);
        }
        return new SessionImpl(metaSession, classLogger, logger, rawWebDriver, httpRequestExecutor);
    }

    public void resetLoggerToAfterAll() {
        logger = null;
        classLoggerMethodName = "afterAll";
        classLoggerDisplayName = "After all";
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

    public InternalSessionImpl createTestSession(final ExtensionContext extensionContext) {
        String methodName = extensionContext.getTestMethod().map(Method::getName).orElse("NO_METHOD_FOUND");
        String displayName = extensionContext.getDisplayName();

        Logger logger = classLogger.createClassLogger(methodName, displayName);

        return new InternalSessionImpl(metaSession, classLogger, logger, objectMapper);
    }
}
