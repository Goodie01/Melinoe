package nz.geek.goodwin.melinoe.framework.api.log;


/**
 * @author Goodie
 */
public interface Logger {
    LogMessage add();

    Logger createSublogger(String title);
}
