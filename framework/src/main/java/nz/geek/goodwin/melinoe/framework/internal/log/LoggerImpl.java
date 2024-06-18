package nz.geek.goodwin.melinoe.framework.internal.log;

import nz.geek.goodwin.melinoe.framework.api.log.LogMessage;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Goodie
 */
public class LoggerImpl implements Logger {
    private final List<LogMessage> logMessages;

    public LoggerImpl() {
        logMessages = new ArrayList<>();
    }

    private LoggerImpl(List<LogMessage> logMessages) {
        this.logMessages = logMessages;
    }

    @Override
    public LogMessage add() {
        LogMessage logMessage = new LogMessageImpl(LocalDateTime.now());
        logMessages.add(logMessage);

        return logMessage;
    }

    @Override
    public LoggerImpl createSublogger(final String title) {
        List<LogMessage> subSessionLogger = new ArrayList<>();
        add().withSubSessionMessages(subSessionLogger).withMessage(title);

        return new LoggerImpl(subSessionLogger);
    }

    @Override
    public Logger createSublogger(String title, String image) {
        List<LogMessage> subSessionLogger = new ArrayList<>();
        add().withSubSessionMessages(subSessionLogger).withMessage(title).withImage(image);

        return new LoggerImpl(subSessionLogger);
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }
}
