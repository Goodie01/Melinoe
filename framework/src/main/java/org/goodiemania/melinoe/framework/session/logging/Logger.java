package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private final boolean isSubSessionLogger;
    private final String displayName;
    private final String packageName;
    private final String className;
    private final String methodName;
    private final List<LogMessage> logMessages = new ArrayList<>();

    private final File logFile;

    public Logger(final File logFile,
                  final boolean isSubSessionLogger,
                  final String displayName,
                  final String packageName,
                  final String className,
                  final String methodName) {
        this.logFile = logFile;
        this.isSubSessionLogger = isSubSessionLogger;
        this.displayName = displayName;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
    }

    public LogMessage add() {
        LogMessage logMessage = new LogMessage(LocalDateTime.now());
        logMessages.add(logMessage);

        return logMessage;
    }

    public File getLogFile() {
        return logFile;
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

    public String getMethodName() {
        return methodName;
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public boolean getHasPassed() {
        for (LogMessage message : logMessages) {
            if (message.getFail()) {
                return false;
            }
        }
        return true;
    }

    public boolean isSubSessionLogger() {
        return isSubSessionLogger;
    }
}
