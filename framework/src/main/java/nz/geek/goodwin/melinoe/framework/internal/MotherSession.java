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
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Goodie
 *
 * Internal only
 */
public class MotherSession {
    private static MotherSession SESSION;
    private final LogFileManager fileManager;
    private final LoggerImpl logger;
    private final WebDriverRegister webDriverRegister;
    private final ObjectMapper objectMapper;

    private MotherSession() {
        webDriverRegister = new WebDriverRegister();
        fileManager = new LogFileManager();
        logger = new LoggerImpl(fileManager);

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
        String logMessage;
        switch (MelinoeExtension.EXECUTION_TYPE) {
            case BEFORE_ALL -> {
                logMessage = "Before all";
            }
            case BEFORE_EACH -> {
                logMessage = "Before each";
            }
            case TEST -> {
                logMessage = MelinoeExtension.DISPLAY_NAME + System.lineSeparator() + MelinoeExtension.METHOD_NAME + "()";
            }
            case AFTER_EACH -> {
                logMessage = "After each";
            }
            case AFTER_ALL -> {
                logMessage = "After all";
            }
            default -> {
                logMessage = "Other???";
            }
        }

        return new SessionImpl(fileManager, logger.createSublogger(logMessage), webDriverRegister);
    }

    public void closeAll() {
        try {
            String jsonOutput = objectMapper.writer().writeValueAsString(logger.getLogMessages());
            writeLogJsonFile(jsonOutput);
            writeLogHtmlFile(jsonOutput);
        } catch (IOException e) {
            throw new MelinoeException(e);
        } finally {
            webDriverRegister.getWebDrivers().forEach(WebDriver::quit);
        }
    }

    private void writeLogJsonFile(final String logJson) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(fileManager.getLogFile())) {
            out.println(logJson);
        }
    }

    private void writeLogHtmlFile(final String logJson) throws IOException {
        try (PrintWriter out = new PrintWriter(fileManager.getLogHtmlFile())) {
            InputStream resourceAsStream = MotherSession.class.getClassLoader().getResourceAsStream("html/logsTest.html");
            String htmlOutput = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);

            htmlOutput = htmlOutput.replace("let log = []","let log =" + logJson);

            out.print(htmlOutput);
        }
    }

}
