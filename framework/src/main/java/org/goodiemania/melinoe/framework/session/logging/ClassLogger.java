package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ClassLogger {
    private final File logFile;
    private List<Logger> loggers = new ArrayList<>();
    private LogFileManager fileManager;
    private String displayName;
    private String packageName;
    private String className;
    private boolean hasPassed = true;

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
        return hasPassed;
    }

    public List<Logger> getLoggers() {
        return loggers;
    }

    public Logger createClassLogger() {
        String methodName = "STATIC";

        Logger logger = new Logger(fileManager, displayName, packageName, className, methodName);
        loggers.add(logger);
        return logger;
    }

    public Logger createClassLogger(final ExtensionContext extensionContext) {
        String methodName = extensionContext.getTestMethod().map(Method::getName).orElse("NO_METHOD_FOUND");

        Logger logger = new Logger(fileManager, displayName, packageName, className, methodName);
        loggers.add(logger);
        return logger;
    }

    public File getLogFile() {
        return logFile;
    }
}
