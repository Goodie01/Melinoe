package org.goodiemania.melinoe.framework.session;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.goodiemania.melinoe.framework.InternalSession;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.writer.LogWriter;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MetaSession {
    private Set<ClosableDriver> drivers = new HashSet<>();
    private Map<String, ClassLogger> logs = new HashMap<>();

    public MetaSession() {
    }

    public void addDriver(final ClosableDriver driver) {
        drivers.add(driver);
    }

    public InternalSession createSessionFor(final ExtensionContext extensionContext) {
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

        ClassLogger classLogger = new ClassLogger(extensionContext.getDisplayName(), packageName, className, methodName, fullMethodName);

        logs.put(classLogger.getFullMethodName(), classLogger);

        return new InternalSession(this, classLogger);
    }

    public void logStuff() {
        new LogWriter().write(logs);
    }

    public void endSession() {
        Set<ClosableDriver> soonToBeClosedDrivers = drivers;
        drivers = new HashSet<>();
        soonToBeClosedDrivers.forEach(ClosableDriver::close);
    }
}
