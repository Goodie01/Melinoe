package org.goodiemania.melinoe.framework.session;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.goodiemania.melinoe.framework.SessionImpl;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MetaSession {
    private List<ClosableDriver> drivers = new ArrayList<>();
    private Map<String, ClassLogger> logs = new HashMap<>();

    public MetaSession() {
        System.out.println("Creating a new meta session: " + this.hashCode());
    }

    public void addDriver(final ClosableDriver driver) {
        drivers.add(driver);
    }

    public List<ClosableDriver> getDrivers() {
        return drivers;
    }

    public SessionImpl createSessionFor(final ExtensionContext extensionContext) {
        String className = extensionContext.getTestClass()
                .map(Class::getName)
                .orElse("NO_CLASS_NAME");

        String packageName = extensionContext.getTestMethod()
                .map(Method::getName)
                .map(name -> className + "." + name)
                .orElse(className);

        ClassLogger classLogger = new ClassLogger(extensionContext.getDisplayName(), packageName);

        logs.put(classLogger.getPackageName(), classLogger);

        return new SessionImpl(this, classLogger);
    }

    public void logStuff() {
        logs.forEach((s, classLogger) -> {
            System.out.println("======================  " + s + "  ++++++++++++++++++++");

            classLogger.getLogMessages().forEach(logMessage -> {
                System.out.println(logMessage.getMessage() + "  ------  " + logMessage.getSecondMessage());
            });
        });
    }

    public void endSession() {
        List<ClosableDriver> soonToBeClosedDrivers = drivers;
        drivers = new ArrayList<>();
        soonToBeClosedDrivers.forEach(ClosableDriver::close);

        //TODO write logs

    }
}
