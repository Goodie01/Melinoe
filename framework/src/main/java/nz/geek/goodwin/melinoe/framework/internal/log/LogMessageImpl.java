package nz.geek.goodwin.melinoe.framework.internal.log;

import nz.geek.goodwin.melinoe.framework.api.log.LogMessage;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class LogMessageImpl implements LogMessage {
    private final LocalDateTime dateTime;
    private String message;
    private Throwable throwable;
    private String image;
    private List<LogMessage> subSessionMessages;
    private boolean success = true;

    public LogMessageImpl(final LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public LogMessage withMessage(final String message) {
        if (StringUtils.isEmpty(this.message)) {
            this.message = message;
        } else {
            this.message = this.message + System.lineSeparator() + message;
        }

        return this;
    }

    @Override
    public LogMessage withThrowable(final Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public LogMessage withImage(final String image) {
        this.image = image;
        return this;
    }

    @Override
    public List<LogMessage> getSubSessionMessages() {
        return subSessionMessages;
    }

    @Override
    public LogMessage withSubSessionMessages(final List<LogMessage> subSessionLogger) {
        this.subSessionMessages = subSessionLogger;
        return this;
    }

    @Override
    public LogMessage withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean isSuccess() {
        if (subSessionMessages != null) {
            return !subSessionMessages.stream().anyMatch(logMessage -> !logMessage.isSuccess()) && success;
        }
        return success;
    }
}
