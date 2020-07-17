package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ClassLogger {
    private final File logFile;
    private final List<Logger> loggers = new ArrayList<>();
    private final LogFileManager fileManager;
    private final String displayName;
    private final String packageName;
    private final String className;

    public ClassLogger(final LogFileManager fileManager, final String displayName, final String packageName, final String className) {
        this.fileManager = fileManager;
        this.displayName = displayName;
        this.packageName = packageName;
        this.className = className;

        logFile = fileManager.createLogFile(className);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public Boolean getHasPassed() {
        for (Logger logger : loggers) {
            if (!logger.getHasPassed()) {
                return false;
            }
        }
        return true;
    }

    public List<Logger> getLoggers() {
        return loggers;
    }

    public Logger createClassLogger() {
        String methodName = "STATIC";

        Logger logger = new Logger(fileManager, "Before all", packageName, className, methodName);
        loggers.add(logger);
        return logger;
    }

    public Logger createClassLogger(final ExtensionContext extensionContext) {
        String methodName = extensionContext.getTestMethod().map(Method::getName).orElse("NO_METHOD_FOUND");
        String displayName = extensionContext.getDisplayName();

        Logger logger = new Logger(fileManager, displayName, packageName, className, methodName);
        loggers.add(logger);
        return logger;
    }

    public File getLogFile() {
        return logFile;
    }
}
