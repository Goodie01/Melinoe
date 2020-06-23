package org.goodiemania.melinoe.framework.session.logging;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClassLogger {
    private String displayName;
    private String packageName;
    private String fullMethodName;
    private List<LogMessage> logMessages;
    private Boolean hasPassed;

    public ClassLogger(final String displayName, final String packageName, final String fullMethodName) {
        this.displayName = displayName;
        this.packageName = packageName;
        this.fullMethodName = fullMethodName;

        this.logMessages = new ArrayList<>();
        this.hasPassed = true;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public Boolean getHasPassed() {
        return hasPassed;
    }

    public void fail() {
        hasPassed = false;
    }

    public void add(final String message) {
        LogMessage logMessage = new LogMessage(LocalDateTime.now(), message, "");
        logMessages.add(logMessage);
    }

    public void add(final String message, final String extraInfo) {
        LogMessage logMessage = new LogMessage(LocalDateTime.now(), message, extraInfo);
        logMessages.add(logMessage);
    }

    public String getPackageName() {
        return packageName;
    }
}
