package nz.geek.goodwin.melinoe.framework.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import nz.geek.goodwin.melinoe.framework.internal.log.LoggerImpl;
import nz.geek.goodwin.melinoe.framework.internal.web.WebDriverRegister;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

/**
 * @author Goodie
 *
 * Internal only
 */
public class MotherSession {
    private static MotherSession SESSION;
    private final LogFileManager logFileManager;
    private final LoggerImpl logger;
    private final WebDriverRegister webDriverRegister;
    private final ObjectMapper objectMapper;

    private MotherSession() {
        webDriverRegister = new WebDriverRegister();
        logFileManager = new LogFileManager();
        logger = new LoggerImpl(logFileManager);

        objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    public static MotherSession getInstance() {
        if(SESSION == null) {
            SESSION = new MotherSession();
        }
        return SESSION;
    }

    public Session newSession() {
        return new SessionImpl(logFileManager, logger.createSublogger("Test 1"), webDriverRegister);
    }

    public void closeAll() {
        try {
            objectMapper.writer().writeValue(logFileManager.createRootLogFile(), logger.getLogMessages());
        } catch (IOException e) {
            throw new MelinoeException(e);
        } finally {
            webDriverRegister.getWebDrivers().forEach(WebDriver::quit);
        }
    }
}
