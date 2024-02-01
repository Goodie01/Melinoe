package nz.geek.goodwin.melinoe.framework.internal.log;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class LogMessage {
    private final LocalDateTime dateTime;
    private String message;
    private Throwable throwable;
    private File image;
    private List<LogMessage> subSessionMessages;
    private boolean success = true;

    public LogMessage(final LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
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

    public LogMessage withThrowable(final Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public LogMessage withImage(final File image) {
        this.image = image;
        return this;
    }

    protected List<LogMessage> getSubSessionMessages() {
        return subSessionMessages;
    }

    protected LogMessage withSubSessionMessages(final List<LogMessage> subSessionLogger) {
        this.subSessionMessages = subSessionLogger;
        return this;
    }

    public void withSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
