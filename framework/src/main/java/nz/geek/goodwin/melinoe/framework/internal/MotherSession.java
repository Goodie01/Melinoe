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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Goodie
 *
 * Internal only
 */
public class MotherSession {
    private static MotherSession SESSION;
    private final LogFileManager fileManager;
    private final LoggerImpl rootLogger;
    private final Map<String, LoggerImpl> classLoggersMap;
    private final Map<String, LoggerImpl> methodLoggersMap;
    private final WebDriverRegister webDriverRegister;
    private final ObjectMapper objectMapper;

    private MotherSession() {
        webDriverRegister = new WebDriverRegister();
        fileManager = new LogFileManager();
        rootLogger = new LoggerImpl(fileManager);
        classLoggersMap = new HashMap<>();
        methodLoggersMap = new HashMap<>();

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
        String prettyName = MelinoeExtension.DISPLAY_NAME + System.lineSeparator() + MelinoeExtension.METHOD_NAME + "()";

        String logMessage = switch (MelinoeExtension.EXECUTION_TYPE) {
            case BEFORE_ALL -> "Before all";
            case BEFORE_EACH -> "Before each";
            case AFTER_EACH -> "After each";
            case AFTER_ALL -> "After all";
            default -> "Other???";
        };

        LoggerImpl classLogger = classLoggersMap.computeIfAbsent(MelinoeExtension.CLASS_NAME, s -> rootLogger.createSublogger(MelinoeExtension.CLASS_NAME));

        if(MelinoeExtension.EXECUTION_TYPE == MelinoeExtension.Type.BEFORE_ALL || MelinoeExtension.EXECUTION_TYPE == MelinoeExtension.Type.AFTER_ALL) {
            return new SessionImpl(fileManager, classLogger.createSublogger(logMessage), webDriverRegister);
        } else {
            LoggerImpl methodLogger = methodLoggersMap.computeIfAbsent(MelinoeExtension.CLASS_NAME + MelinoeExtension.METHOD_NAME, s -> classLogger.createSublogger(prettyName));
            if(MelinoeExtension.EXECUTION_TYPE == MelinoeExtension.Type.BEFORE_EACH || MelinoeExtension.EXECUTION_TYPE == MelinoeExtension.Type.AFTER_EACH) {
                return new SessionImpl(fileManager, methodLogger.createSublogger(logMessage), webDriverRegister);
            }
            return new SessionImpl(fileManager, methodLogger, webDriverRegister);
        }
    }

    public void closeAll() {
        try {
            String jsonOutput = objectMapper.writer().writeValueAsString(rootLogger.getLogMessages());
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
