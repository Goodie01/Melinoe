package nz.geek.goodwin.melinoe.framework.internal;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import nz.geek.goodwin.melinoe.framework.internal.log.Logger;
import nz.geek.goodwin.melinoe.framework.internal.web.WebDriverRegister;

/**
 * @author Goodie
 *
 * Internal only
 */
public class MotherSession {
    private static MotherSession SESSION;
    private final LogFileManager logFileManager;
    private final Logger logger;
    private final WebDriverRegister webDriverRegister;

    private MotherSession() {
        webDriverRegister = new WebDriverRegister();
        logFileManager = new LogFileManager();
        logger = new Logger(logFileManager);
    }

    public static MotherSession getInstance() {
        if(SESSION == null) {
            SESSION = new MotherSession();
        }
        return SESSION;
    }

    public Session newSession() {
        return new SessionImpl(logFileManager, logger, webDriverRegister);
    }

    public void close() {
        webDriverRegister.getWebDrivers().forEach(webDriver -> {
            webDriver.close();
            webDriver.quit();
        });
    }
}
