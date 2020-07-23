package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.time.LocalDateTime;

public class LogMessage {
    private final LocalDateTime dateTime;
    private String message = "";
    private String secondMessage = "";
    private String hiddenInfo = "";
    private Throwable throwable = null;
    private boolean fail = false;
    private File image;
    private Logger subSessionLogger;

    public LogMessage(final LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getSecondMessage() {
        return secondMessage;
    }

    public String getHiddenInfo() {
        return hiddenInfo;
    }

    public boolean getFail() {
        return fail;
    }

    public File getImage() {
        return image;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public LogMessage withMessage(final String message) {
        this.message = message;
        return this;
    }

    public LogMessage withSecondMessage(final String secondMessage) {
        this.secondMessage = secondMessage;
        return this;
    }

    public LogMessage withHiddenInfo(final String hiddenInfo) {
        this.hiddenInfo = hiddenInfo;
        return this;
    }

    public LogMessage fail() {
        this.fail = true;
        return this;
    }

    public LogMessage withThrowable(final Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public LogMessage withImage(final File image) {
        this.image = image;
        return this;
    }

    public Logger getSubSessionLogger() {
        return subSessionLogger;
    }

    public LogMessage withSubSessionLogger(final Logger subSessionLogger) {
        this.subSessionLogger = subSessionLogger;
        return this;
    }
}
