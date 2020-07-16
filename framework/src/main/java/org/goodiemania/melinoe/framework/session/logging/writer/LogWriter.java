package org.goodiemania.melinoe.framework.session.logging.writer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.session.logging.MetaLogger;

public class LogWriter {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public void write(final MetaLogger logs) {
        createRootLogFile(logs);

        logs.getClassLoggers().forEach(classLogger -> {
            createClassLogFile(classLogger);

            classLogger.getLoggers().forEach(this::createLogFile);
        });
    }

    private void createLogFile(final Logger classLogger) {
        List<String> processedLogs = classLogger.getLogMessages().stream()
                .map(logMessage -> {
                    if (StringUtils.isBlank(logMessage.getHiddenInfo())) {
                        return String.format(LogWriterConstants.INDIVIDUAL_SECTION_LOG_HTML,
                                logMessage.getDateTime(),
                                logMessage.getMessage(),
                                logMessage.getSecondMessage());
                    } else {
                        String hiddenSectionId = randomAlphaNumeric();
                        return String.format(LogWriterConstants.INDIVIDUAL_SECTION_HIDDEN_LOG_HTML,
                                hiddenSectionId,
                                logMessage.getDateTime(),
                                logMessage.getMessage(),
                                logMessage.getSecondMessage(),
                                hiddenSectionId,
                                logMessage.getHiddenInfo());
                    }
                })
                .collect(Collectors.toList());

        processedLogs.add(0, LogWriterConstants.HTML_HEAD);
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(classLogger.getLogFile(), processedLogs);
    }

    private void createClassLogFile(final ClassLogger classLogger) {
        List<String> processedLogs = classLogger.getLoggers().stream()
                .map(logger -> {
                    String passColor = logger.getHasPassed() ? "#00ff7b" : "#ff007b";

                    return String.format(LogWriterConstants.INDIVIDUAL_SECTION_HTML,
                            "file://" + logger.getLogFile().getAbsolutePath(),
                            passColor,
                            logger.getDisplayName(),
                            logger.getClassName() + "." + logger.getMethodName());
                })
                .collect(Collectors.toList());

        processedLogs.add(0, LogWriterConstants.HTML_HEAD);
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(classLogger.getLogFile(), processedLogs);
    }

    private void createRootLogFile(final MetaLogger logs) {
        List<String> processedLogs = logs.getClassLoggers().stream()
                .map(classLogger -> {
                    String passColor = classLogger.getHasPassed() ? "#00ff7b" : "#ff007b";

                    return String.format(LogWriterConstants.INDIVIDUAL_SECTION_HTML,
                            "file://" + classLogger.getLogFile().getAbsolutePath(),
                            passColor,
                            classLogger.getDisplayName(),
                            classLogger.getClassName());
                })
                .collect(Collectors.toList());

        processedLogs.add(0, LogWriterConstants.HTML_HEAD);
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(logs.getRootLogFile(), processedLogs);
    }

    private void writeLines(final File logFile, final List<String> processedLogs) {
        try {
            FileUtils.writeLines(logFile, processedLogs);
        } catch (IOException e) {
            throw new IllegalStateException("Error writing lines to log file");
        }
    }

    private static String randomAlphaNumeric() {
        int count = 10;
        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }

        return builder.toString();

    }
}
