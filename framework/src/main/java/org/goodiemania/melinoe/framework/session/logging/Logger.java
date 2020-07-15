package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private LogFileManager logFileManager;
    private String displayName;
    private String packageName;
    private String className;
    private String methodName;
    private List<LogMessage> logMessages = new ArrayList<>();
    private Boolean hasPassed = true;

    private File logFile;

    public Logger(final LogFileManager logFileManager,
                  final String displayName,
                  final String packageName,
                  final String className,
                  final String methodName) {
        this.logFileManager = logFileManager;
        this.displayName = displayName;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;

        this.logFile = logFileManager.createLogFile(className, methodName);
    }

    public void add(final String message) {
        add(message, "");
    }

    public void addWithHiddenInfo(final String message, final String hiddenInfo) {
        LogMessage logMessage = new LogMessage(LocalDateTime.now(), message, "", hiddenInfo);
        logMessages.add(logMessage);
    }

    public void add(final String message, final String extraInfo) {
        LogMessage logMessage = new LogMessage(LocalDateTime.now(), message, extraInfo, "");
        logMessages.add(logMessage);
    }

    public void addWithImage(final String message, final File imageFile) {
        File newImageFile = logFileManager.createImageFile(imageFile);

        String imageString = String.format("<a href='%s'><img width='200' src='%s'></a>",
                "file:///" + newImageFile.getAbsolutePath(),
                "file:///" + newImageFile.getAbsolutePath()
        );

        LogMessage logMessage = new LogMessage(LocalDateTime.now(), message, imageString, "");
        logMessages.add(logMessage);
    }

    public void fail() {
        hasPassed = false;
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

    public Boolean getHasPassed() {
        return hasPassed;
    }
}
