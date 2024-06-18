package nz.geek.goodwin.melinoe.framework.internal;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.rest.RestDriver;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import nz.geek.goodwin.melinoe.framework.internal.rest.RestDriverImpl;
import nz.geek.goodwin.melinoe.framework.internal.web.WebDriverImpl;
import nz.geek.goodwin.melinoe.framework.internal.web.ClosableRegister;

/**
 * @author Goodie
 */
public class SessionImpl implements Session {
    private final LogFileManager logFileManager;
    private final Logger logger;
    private final ClosableRegister closableRegister;
    private WebDriverImpl webDriver;
    private RestDriverImpl restDriver;

    public SessionImpl(LogFileManager logFileManager, Logger logger, ClosableRegister closableRegister) {
        this.logFileManager = logFileManager;
        this.logger = logger;
        this.closableRegister = closableRegister;
    }

    @Override
    public Session createChildSession(String name) {
        return new SessionImpl(logFileManager, logger.createSublogger(name), closableRegister);
    }

    @Override
    public RestDriver rest() {
        if(restDriver == null) {
            restDriver = new RestDriverImpl(logger, closableRegister);
        }

        return restDriver;
    }

    @Override
    public WebDriver web() {
        if(webDriver == null) {
            webDriver = new WebDriverImpl(this, logFileManager, logger, closableRegister);
        }
        return webDriver;
    }

    @Override
    public Logger log() {
        return logger;
    }
}
