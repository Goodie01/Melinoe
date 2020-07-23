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
    private final HttpClient httpClient;

    private Logger logger;
    private String classLoggerMethodName = "beforeAll";
    private String classLoggerDisplayName = "Before all";


    public InternalSessionClassImpl(final MetaSession metaSession, final ClassLogger classLogger) {
        this.metaSession = metaSession;
        this.classLogger = classLogger;

        this.httpClient = HttpClient.newBuilder().build();

        this.logger = classLogger.createClassLogger(classLoggerMethodName, classLoggerDisplayName);


        this.rawWebDriver = new RawWebDriver(metaSession, logger, "internal class session 1");
        this.httpRequestExecutor = new HttpRequestExecutor(this);

        this.flowDecorator = new FlowDecorator(getSession(), rawWebDriver);
        this.objectMapper = new ObjectMapper();
    }

    public InternalSession createTestSession(final ExtensionContext extensionContext) {
        String methodName = extensionContext.getTestMethod().map(Method::getName).orElse("NO_METHOD_FOUND");
        String displayName = extensionContext.getDisplayName();

        Logger logger = classLogger.createClassLogger(methodName, displayName);

        RawWebDriver rawWebDriver = new RawWebDriver(metaSession, logger, "internal class session 2");
        HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor(this);
        //TODO the bug is heeeeere
        //  The flow decorator starts passing in THIS class session into places, when actually we want it to pass in the new session we're creating...
        //  Ultimately I think we also want to think about splitting internal session into a....
        //      'internal session' with meta session,  object mapper, http client etc on it,
        //      and a 'internal session' with the drivers on it
        FlowDecorator flowDecorator = new FlowDecorator(getSession(), rawWebDriver);

        return new InternalSessionImpl(this, rawWebDriver, httpRequestExecutor, flowDecorator, logger);
    }

    public void resetLoggerToAfterAll() {
        logger = null;
        classLoggerMethodName = "afterAll";
        classLoggerDisplayName = "After all";
    }

    @Override
    public Session getSession() {
        if (this.logger == null) {
            this.logger = classLogger.createClassLogger(classLoggerMethodName, classLoggerDisplayName);
        }
        return new SessionImpl(this, logger, rawWebDriver, httpRequestExecutor, flowDecorator);
    }

    @Override
    public MetaSession getMetaSession() {
        return metaSession;
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

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public ClassLogger getClassLogger() {
        return classLogger;
    }
}
