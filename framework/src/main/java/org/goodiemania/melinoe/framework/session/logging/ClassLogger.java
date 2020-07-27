package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public boolean getHasPassed() {
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

    public Logger createClassLogger(final String methodName, final String displayName) {
        final File newLogFile = fileManager.createLogFile(className, methodName);
        Logger logger = new Logger(newLogFile, false, displayName, packageName, className, methodName);
        loggers.add(logger);

        return logger;
    }

    public Logger createSubSessionLogger(final String methodName) {
        final File newLogFile = fileManager.createLogFile(className, methodName + "_SubSession_" + UUID.randomUUID().toString());
        Logger logger = new Logger(newLogFile, true, displayName, packageName, className, methodName);
        loggers.add(logger);

        return logger;
    }

    public File getLogFile() {
        return logFile;
    }
}
