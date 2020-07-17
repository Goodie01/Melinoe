package org.goodiemania.melinoe.framework.session.logging.writer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.LogMessage;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.session.logging.MetaLogger;

public class LogWriter {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public void write(final MetaLogger logs) {
        createRootLogFile(logs);

        logs.getClassLoggers().forEach(classLogger -> {
            createClassLogFile(classLogger);

            classLogger.getLoggers().forEach(logger -> createLogFile(logs, logger));
        });
    }

    private void createLogFile(final MetaLogger metaLogger, final Logger classLogger) {
        List<String> processedLogs = classLogger.getLogMessages().stream()
                .map(logMessage -> {
                    String secondMessage = processSecondText(metaLogger, logMessage);
                    String hiddenString = processHiddenText(logMessage);
                    if (StringUtils.isBlank(hiddenString)) {
                        return String.format(LogWriterConstants.INDIVIDUAL_SECTION_LOG_HTML,
                                logMessage.getDateTime(),
                                logMessage.getMessage(),
                                secondMessage);
                    } else {
                        String hiddenSectionId = randomAlpha();

                        return String.format(LogWriterConstants.INDIVIDUAL_SECTION_HIDDEN_LOG_HTML,
                                hiddenSectionId,
                                logMessage.getDateTime(),
                                logMessage.getMessage(),
                                secondMessage,
                                hiddenSectionId,
                                hiddenString);
                    }
                })
                .collect(Collectors.toList());

        processedLogs.add(0, LogWriterConstants.HTML_HEAD);
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(classLogger.getLogFile(), processedLogs);
    }

    private String processSecondText(final MetaLogger metaLogger, final LogMessage logMessage) {
        if (logMessage.getImage() != null) {
            if (!logMessage.isImageCopied()) {
                File newImageFile = metaLogger.getFileManager().createImageFile(logMessage.getImage());

                logMessage.withImage(newImageFile)
                        .withImageCopied(true);
            }

            return String.format("<a href='%s'><img width='200' src='%s'></a>",
                    "file:///" + logMessage.getImage().getAbsolutePath(),
                    "file:///" + logMessage.getImage().getAbsolutePath()
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

    private static String randomAlpha() {
        int count = 10;
        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }

        return builder.toString();

    }
}
