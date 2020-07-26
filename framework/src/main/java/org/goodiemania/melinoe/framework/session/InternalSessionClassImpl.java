package org.goodiemania.melinoe.framework.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
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
        this.rawWebDriver = new RawWebDriver(metaSession, logger, "internal class session 1a");
        this.httpRequestExecutor = new HttpRequestExecutor(this);
    }

    public InternalSession createTestSession(final ExtensionContext extensionContext) {
        String methodName = extensionContext.getTestMethod().map(Method::getName).orElse("NO_METHOD_FOUND");
        String displayName = extensionContext.getDisplayName();

        Logger logger = classLogger.createClassLogger(methodName, displayName);

        RawWebDriver rawWebDriver = new RawWebDriver(metaSession, logger, "internal class session 2");
        HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor(
                getHttpClient(),
                logger,
                getObjectMapper());

        return new InternalSessionImpl(
                getMetaSession(),
                getClassLogger(),
                rawWebDriver,
                httpRequestExecutor,
                logger);
    }

    public void resetLoggerToAfterAll() {
        this.logger = classLogger.createClassLogger("afterAll", "After all");
        this.rawWebDriver = new RawWebDriver(metaSession, logger, "internal class session 1b");
        this.httpRequestExecutor = new HttpRequestExecutor(this);
    }

    @Override
    public Session getSession() {
        return new SessionImpl(this, logger, rawWebDriver, httpRequestExecutor);
    }

    @Override
    public MetaSession getMetaSession() {
        return metaSession;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return metaSession.getObjectMapper();
    }

    @Override
    public HttpClient getHttpClient() {
        return metaSession.getHttpClient();
    }

    @Override
    public ClassLogger getClassLogger() {
        return classLogger;
    }
}
