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

        this.rawWebDriver = new RawWebDriver(metaSession, this);
        this.flowDecorator = new FlowDecorator(this);
        this.objectMapper = new ObjectMapper();
        this.httpRequestExecutor = new HttpRequestExecutor(this, HttpClient.newBuilder().build());
    }

    @Override
    public Session getSession() {
        if (this.logger == null) {
            this.logger = classLogger.createClassLogger(classLoggerMethodName, classLoggerDisplayName);
        }
        return new SessionImpl(logger, flowDecorator, rawWebDriver, httpRequestExecutor);
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
        String packageName = extensionContext.getTestClass()
                .map(Class::getPackageName)
                .orElse("NO_PACKAGE");

        String className = extensionContext.getTestClass()
                .map(Class::getName)
                .orElse("NO_CLASS_NAME");

        String methodName = extensionContext.getTestMethod()
                .map(Method::getName)
                .orElse("NO_METHOD_NAME");

        String fullMethodName = extensionContext.getTestMethod()
                .map(Method::getName)
                .map(name -> className + "." + methodName)
                .orElse(className);

        //TODO rewrite this to create the logger from the above
        Logger logger = classLogger.createClassLogger(extensionContext);

        return new InternalSessionImpl(metaSession, logger, objectMapper);
    }
}
