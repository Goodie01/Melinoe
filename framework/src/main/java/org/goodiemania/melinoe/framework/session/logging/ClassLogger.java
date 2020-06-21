package org.goodiemania.melinoe.framework.session.logging;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClassLogger {
    private String name;
    private String packageName;
    private List<LogMessage> logMessages;
    private Boolean hasPassed;

    public ClassLogger(final String name, final String packageName) {
        this.name = name;
        this.packageName = packageName;

        logMessages = new ArrayList<>();
        hasPassed = true;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public Boolean getHasPassed() {
        return hasPassed;
    }

    public void fail() {
        hasPassed = false;
        //TODO This needs to cascade upwards
    }

    public void add(final String message) {
        LogMessage logMessage = new LogMessage(LocalDateTime.now(), message, "");
        logMessages.add(logMessage);
    }

    public void add(final String message, final String extraInfo) {
        LogMessage logMessage = new LogMessage(LocalDateTime.now(), message, extraInfo);
        logMessages.add(logMessage);
    }
}
