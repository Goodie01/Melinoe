package org.goodiemania.melinoe.framework.session;

import java.lang.reflect.Method;
import org.goodiemania.melinoe.framework.junit.FlowDecorator;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.junit.jupiter.api.extension.ExtensionContext;

public class InternalSessionClassImpl implements InternalSession {
    private MetaSession metaSession;
    private final ClassLogger classLogger;
    private final FlowDecorator flowDecorator;
    private final RawWebDriver rawWebDriver;


    public InternalSessionClassImpl(final MetaSession metaSession, final ClassLogger classLogger) {
        this.metaSession = metaSession;
        this.classLogger = classLogger;

        this.rawWebDriver = new RawWebDriver(metaSession, this);
        this.flowDecorator = new FlowDecorator(this);
    }

    @Override
    public Session getSession() {
        return new SessionImpl(classLogger.createClassLogger(), rawWebDriver);
    }

    @Override
    public FlowDecorator getFlowDecorator() {
        return flowDecorator;
    }

    @Override
    public RawWebDriver getRawWebDriver() {
        return rawWebDriver;
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

        return new InternalSessionImpl(metaSession, logger);
    }
}
