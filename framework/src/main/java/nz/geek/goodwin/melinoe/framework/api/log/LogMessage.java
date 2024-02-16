package nz.geek.goodwin.melinoe.framework.api.log;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Goodie
 */
public interface LogMessage {
    LocalDateTime getDateTime();

    String getMessage();

    String getImage();

    Throwable getThrowable();

    LogMessage withMessage(String message);

    LogMessage withThrowable(Throwable throwable);

    LogMessage withImage(String image);

    List<LogMessage> getSubSessionMessages();

    LogMessage withSubSessionMessages(List<LogMessage> subSessionLogger);

    LogMessage withSuccess(boolean success);

    boolean isSuccess();
}
