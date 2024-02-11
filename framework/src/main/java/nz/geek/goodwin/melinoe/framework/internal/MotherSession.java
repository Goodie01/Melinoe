package nz.geek.goodwin.melinoe.framework.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import nz.geek.goodwin.melinoe.framework.internal.log.LoggerImpl;
import nz.geek.goodwin.melinoe.framework.internal.web.WebDriverRegister;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTimeDeserializer dateTimeDeserializer = new LocalDateTimeDeserializer(formatter);
        LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(formatter);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, dateTimeDeserializer);
        javaTimeModule.addSerializer(LocalDateTime.class, dateTimeSerializer);

        objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule);
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
            objectMapper.writer().writeValue(logFileManager.getLogFile(), logger.getLogMessages());
        } catch (IOException e) {
            throw new MelinoeException(e);
        } finally {
            webDriverRegister.getWebDrivers().forEach(WebDriver::quit);
        }
    }
}
