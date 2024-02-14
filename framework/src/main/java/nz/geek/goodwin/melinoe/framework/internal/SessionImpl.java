package nz.geek.goodwin.melinoe.framework.internal;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import nz.geek.goodwin.melinoe.framework.internal.web.WebDriverImpl;
import nz.geek.goodwin.melinoe.framework.internal.web.WebDriverRegister;

/**
 * @author Goodie
 */
public class SessionImpl implements Session {
    private final LogFileManager logFileManager;
    private final Logger logger;
    private final WebDriverRegister webDriverRegister;
    private WebDriverImpl webDriver;

    public SessionImpl(LogFileManager logFileManager, Logger logger, WebDriverRegister webDriverRegister) {
        this.logFileManager = logFileManager;
        this.logger = logger;
        this.webDriverRegister = webDriverRegister;

        System.out.println("DISP: " + MelinoeExtension.DISPLAY_NAME);
        System.out.println("METH: " + MelinoeExtension.METHOD_NAME);
        System.out.println("CLSS: " + MelinoeExtension.CLASS_NAME);
        System.out.println("THRO: " + MelinoeExtension.THROWABLE);
    }

    @Override
    public Session createChildSession(String name) {
        return new SessionImpl(logFileManager, logger.createSublogger(name), webDriverRegister);
    }

    @Override
    public WebDriver web() {
        if(webDriver == null) {
            webDriver = new WebDriverImpl(this, logFileManager, logger, webDriverRegister);
        }
        return webDriver;
    }

    @Override
    public Logger log() {
        return logger;
    }
}
