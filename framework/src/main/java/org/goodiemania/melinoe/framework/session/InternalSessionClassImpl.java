package org.goodiemania.melinoe.framework.session;

import java.lang.reflect.Method;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;

public class InternalSessionClassImpl implements InternalSession {
    private final MetaSession metaSession;
    private final ClassLogger classLogger;

    private Logger logger;
    private RawWebDriver rawWebDriver;
    private HttpRequestExecutor httpRequestExecutor;

    public InternalSessionClassImpl(final MetaSession metaSession, final ClassLogger classLogger) {
        this.metaSession = metaSession;
        this.classLogger = classLogger;

        this.logger = classLogger.createClassLogger("beforeAll", "Before all");
        this.rawWebDriver = new RawWebDriver(this);
        this.httpRequestExecutor = new HttpRequestExecutor(this);
    }

    public InternalSession createTestSession(final ExtensionContext extensionContext) {
        String methodName = extensionContext.getTestMethod().map(Method::getName).orElse("NO_METHOD_FOUND");
        String displayName = extensionContext.getDisplayName();

        return new InternalSessionImpl(
                metaSession,
                classLogger,
                classLogger.createClassLogger(methodName, displayName));
    }

    public void resetLoggerToAfterAll() {
        this.logger = classLogger.createClassLogger("afterAll", "After all");
        this.rawWebDriver = new RawWebDriver(this);
        this.httpRequestExecutor = new HttpRequestExecutor(this);
    }


    @Override
    public MetaSession getMetaSession() {
        return metaSession;
    }

    @Override
    public Session getSession() {
        return new SessionImpl(this);
    }

    @Override
    public ClassLogger getClassLogger() {
        return classLogger;
    }

    @Override
    public RawWebDriver getRawWebDriver() {
        return rawWebDriver;
    }

    @Override
    public HttpRequestExecutor getHttpRequestExecutor() {
        return httpRequestExecutor;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
