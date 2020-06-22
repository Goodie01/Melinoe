package org.goodiemania.melinoe.framework.session.logging;

import java.time.LocalDateTime;

public class LogMessage {
    private final LocalDateTime dateTime;
    private final String message;
    private final String secondMessage;

    public LogMessage(final LocalDateTime dateTime, final String rawMessage, final String secondMessage) {
        this.dateTime = dateTime;
        this.message = rawMessage;
        this.secondMessage = secondMessage;
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
}
