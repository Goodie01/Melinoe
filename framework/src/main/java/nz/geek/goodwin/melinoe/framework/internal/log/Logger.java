package nz.geek.goodwin.melinoe.framework.internal.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Goodie
 */
public class Logger {
    private final LogFileManager logFileManager;
    private final List<LogMessage> logMessages;

    public Logger(LogFileManager logFileManager) {
        this.logFileManager = logFileManager;
        logMessages = new ArrayList<>();
    }

    private Logger(LogFileManager logFileManager, List<LogMessage> logMessages) {
        this.logFileManager = logFileManager;
        this.logMessages = logMessages;
    }

    public LogMessage add() {
        LogMessage logMessage = new LogMessage(LocalDateTime.now());
        logMessages.add(logMessage);

        return logMessage;
    }

    public Logger createSublogger(final String title) {
        ArrayList<LogMessage> subSessionLogger = new ArrayList<>();
        add().withSubSessionMessages(subSessionLogger).withMessage(title);

        return new Logger(logFileManager, subSessionLogger);
    }
}
