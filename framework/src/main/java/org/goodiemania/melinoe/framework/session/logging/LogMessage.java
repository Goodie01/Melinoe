package org.goodiemania.melinoe.framework.session.logging;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class LogMessage {
    private final LocalDateTime dateTime;
    private final String message;
    private String rawSecondMessage;

    public LogMessage(final LocalDateTime dateTime, final String rawMessage, final String rawSecondMessage) {
        this.dateTime = dateTime;
        this.message = rawMessage;
        this.rawSecondMessage = rawSecondMessage;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getRawSecondMessage() {
        return rawSecondMessage;
    }
}
