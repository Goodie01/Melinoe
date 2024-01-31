package nz.geek.goodwin.melinoe.framework.internal.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Goodie
 */
public class Logger {
    private final LogFileManager logFileManager;
    private final List<LogMessage> logMessages = new ArrayList<>();

    public Logger(LogFileManager logFileManager) {
        this.logFileManager = logFileManager;
    }

    public LogMessage add() {
        LogMessage logMessage = new LogMessage(LocalDateTime.now());
        logMessages.add(logMessage);

        return logMessage;
    }

    public boolean getHasPassed() {
        for (LogMessage message : logMessages) {
            if (message.getFail()) {
                return false;
            }
        }
        return true;
    }
}
