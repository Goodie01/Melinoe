package org.goodiemania.melinoe.framework.session.logging.writer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.goodiemania.melinoe.framework.session.logging.LogFileManager;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.session.logging.MetaLogger;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;

public class LogWriter {
    public void write(final MetaLogger logs) {
        createRootLogFile(logs);

        logs.getClassLoggers().forEach(classLogger -> {
            createClassLogFile(classLogger);

            classLogger.getLoggers().forEach(this::createLogFile);
        });
    }

    private void createLogFile(final Logger classLogger) {
        List<String> processedLogs = classLogger.getLogMessages().stream()
                .map(logMessage -> String.format(LogWriterConstants.INDIVIDUAL_SECTION_LOG_HTML,
                        logMessage.getDateTime(),
                        logMessage.getMessage(),
                        logMessage.getSecondMessage()))
                .collect(Collectors.toList());

        processedLogs.add(0, LogWriterConstants.HTML_HEAD);
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(classLogger.getLogFile(), processedLogs);
    }

    private void createClassLogFile(final ClassLogger classLogger) {
        String passColor = classLogger.getHasPassed() ? "#00ff7b" : "#ff007b";

        List<String> processedLogs = classLogger.getLoggers().stream()
                .map(logger -> String.format(LogWriterConstants.INDIVIDUAL_SECTION_HTML,
                        "file://" + logger.getLogFile().getAbsolutePath(),
                        passColor,
                        logger.getDisplayName(),
                        logger.getClassName() + "." + logger.getMethodName()))
                .collect(Collectors.toList());

        processedLogs.add(0, LogWriterConstants.HTML_HEAD);
        processedLogs.add(LogWriterConstants.HTML_FOOTER);

        writeLines(classLogger.getLogFile(), processedLogs);
    }

    private void createRootLogFile(final MetaLogger logs) {
        String passColor = logs.getHasPassed() ? "#00ff7b" : "#ff007b";

        List<String> processedLogs = logs.getClassLoggers().stream()
                .map(classLogger -> String.format(LogWriterConstants.INDIVIDUAL_SECTION_HTML,
                        "file://" + classLogger.getLogFile().getAbsolutePath(),
                        passColor,
                        classLogger.getDisplayName(),
                        classLogger.getClassName()))
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
}
