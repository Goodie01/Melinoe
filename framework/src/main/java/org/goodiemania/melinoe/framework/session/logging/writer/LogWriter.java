package org.goodiemania.melinoe.framework.session.logging.writer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.LogMessage;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.session.logging.MetaLogger;

public class LogWriter {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random random = new Random();

    public void write(final MetaLogger metaLogger) {
        createCssFile(metaLogger);

        createRootLogFile(metaLogger);

        metaLogger.getClassLoggers().forEach(classLogger -> {
            createClassLogFile(metaLogger, classLogger);

            classLogger.getLoggers().forEach(logger -> createLogFile(metaLogger, logger));
        });
    }

    private void createLogFile(final MetaLogger metaLogger, final Logger logger) {
        File cssFilePath = metaLogger.getCssFile();
        File logFile = logger.getLogFile();

        List<String> processedLogs = logger.getLogMessages().stream()
                .map(logMessage -> {
                    String cssStyle = logMessage.getFail() ? "error-text" : "text-muted";
                    String message = processMessage(logger, logMessage);
                    String secondMessage = processSecondText(logger, logMessage);
                    String hiddenString = processHiddenText(logMessage);
                    if (StringUtils.isBlank(hiddenString)) {
                        return String.format(LogWriterConstants.INDIVIDUAL_SECTION_LOG_HTML,
                                cssStyle,
                                logMessage.getDateTime(),
                                message,
                                secondMessage);
                    } else {
                        String hiddenSectionId = randomAlpha();

                        return String.format(LogWriterConstants.INDIVIDUAL_SECTION_HIDDEN_LOG_HTML,
                                cssStyle,
                                hiddenSectionId,
                                logMessage.getDateTime(),
                                message,
                                secondMessage,
                                hiddenSectionId,
                                hiddenString);
                    }
                })
                .collect(Collectors.toList());

        processedLogs.add(0, String.format(LogWriterConstants.HTML_HEAD,
                getRelativeFileFileLocation(cssFilePath, logFile)));
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(logFile, processedLogs);
    }

    private String processMessage(final Logger logger, final LogMessage logMessage) {
        if (logMessage.getSubSessionLogger() == null) {
            return logMessage.getMessage();
        }

        File logFile = logger.getLogFile();

        return String.format("<a href='%s'>Sub session: %s</a>",
                getRelativeFileFileLocation(logMessage.getSubSessionLogger().getLogFile(), logFile),
                logMessage.getMessage());
    }

    private String processSecondText(final Logger logger, final LogMessage logMessage) {
        File logFile = logger.getLogFile();

        if (logMessage.getImage() != null) {
            return String.format("<a href='%s'><img width='200' src='%s'></a>",
                    getRelativeFileFileLocation(logMessage.getImage(), logFile),
                    getRelativeFileFileLocation(logMessage.getImage(), logFile)
            );
        } else {
            return logMessage.getSecondMessage();
        }
    }

    private String processHiddenText(final LogMessage logMessage) {
        if (logMessage.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            logMessage.getThrowable().printStackTrace(new PrintWriter(sw));

            return sw.toString();
        } else {
            return logMessage.getHiddenInfo();
        }
    }

    private void createClassLogFile(final MetaLogger metaLogger, final ClassLogger classLogger) {
        File cssFilePath = metaLogger.getCssFile();
        File logFile = classLogger.getLogFile();

        List<String> processedLogs = classLogger.getLoggers().stream()
                .map(logger -> {
                    if (logger.isSubSessionLogger()) {
                        return "";
                    }
                    String passColor = logger.getHasPassed() ? "#00ff7b" : "#ff007b";

                    return String.format(LogWriterConstants.INDIVIDUAL_SECTION_HTML,
                            getRelativeFileFileLocation(logger.getLogFile(), logFile),
                            passColor,
                            logger.getDisplayName(),
                            logger.getClassName() + "." + logger.getMethodName());
                })
                .collect(Collectors.toList());

        processedLogs.add(0, String.format(LogWriterConstants.HTML_HEAD,
                getRelativeFileFileLocation(cssFilePath, logFile)));
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(logFile, processedLogs);
    }

    private void createCssFile(final MetaLogger logs) {
        writeLines(
                logs.getCssFile(),
                Collections.singletonList(LogWriterConstants.CSS_FILE_CONTENTS));
    }

    private void createRootLogFile(final MetaLogger logs) {
        File cssFilePath = logs.getCssFile();
        File rootLogFile = logs.getRootLogFile();

        List<String> processedLogs = logs.getClassLoggers().stream()
                .map(classLogger -> {
                    String passColor = classLogger.getHasPassed() ? "#00ff7b" : "#ff007b";

                    return String.format(LogWriterConstants.INDIVIDUAL_SECTION_HTML,
                            getRelativeFileFileLocation(classLogger.getLogFile(), rootLogFile),
                            passColor,
                            classLogger.getDisplayName(),
                            classLogger.getClassName());
                })
                .collect(Collectors.toList());

        processedLogs.add(0, String.format(LogWriterConstants.HTML_HEAD,
                getRelativeFileFileLocation(cssFilePath, rootLogFile)));
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(rootLogFile, processedLogs);
    }

    private static String getRelativeFileFileLocation(final File toFile, final File fromFile) {
        return fromFile.getParentFile().toPath().relativize(toFile.toPath()).toString();
    }

    private static void writeLines(final File logFile, final List<String> processedLogs) {
        try {
            FileUtils.writeLines(logFile, processedLogs);
        } catch (IOException e) {
            throw new MelinoeException("Error writing lines to log file");
        }
    }

    private static String randomAlpha() {
        int count = 10;
        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {
            int character = random.nextInt(ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();

    }
}
