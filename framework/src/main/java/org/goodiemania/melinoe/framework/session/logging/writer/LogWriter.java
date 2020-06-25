package org.goodiemania.melinoe.framework.session.logging.writer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.LogFileManager;
import org.jetbrains.annotations.NotNull;

public class LogWriter {
    //TODO I'm doing a lot of finandaling here... but I could just store them in not in a map but rather something more sensible
    public void write(final Map<String, ClassLogger> logs) {
        LogFileManager instance = LogFileManager.getInstance();
        Set<String> rootLog = new HashSet<>();
        Map<String, Set<String>> classNames = new HashMap<>();

        File rootLogFile = instance.createRootLogFile();

        logs.values().forEach(classLogger -> {
            //write page....
            File logFile = instance.createLogFile(classLogger.getClassName(), classLogger.getMethodName());
            List<String> processedLogs = createMethodLogFile(classLogger);

            writeLines(logFile, processedLogs);
            //Add to root logging page...
            rootLog.add(classLogger.getClassName());

            //add to class logging page...
            Set<String> orDefault = classNames.getOrDefault(classLogger.getClassName(), new HashSet<>());
            classNames.put(classLogger.getClassName(), orDefault);
            orDefault.add(classLogger.getFullMethodName());
        });
    }

    @NotNull
    private List<String> createMethodLogFile(final ClassLogger classLogger) {
        List<String> processedLogs = classLogger.getLogMessages()
                .stream()
                .map(logMessage -> String.format(LogWriterConstants.INDIVIDUAL_SECTION_LOG_HTML,
                        logMessage.getDateTime(),
                        logMessage.getMessage(),
                        logMessage.getSecondMessage()))
                .collect(Collectors.toList());

        String passColor = classLogger.getHasPassed() ? "#00ff7b" : "#ff007b";

        processedLogs.add(0, String.format(LogWriterConstants.INDIVIDUAL_SECTION_HTML,
                passColor,
                classLogger.getDisplayName(),
                classLogger.getFullMethodName()));

        processedLogs.add(0, LogWriterConstants.HTML_HEAD);
        processedLogs.add(LogWriterConstants.HTML_FOOTER);
        return processedLogs;
    }

    private void writeLines(final File logFile, final List<String> processedLogs) {
        try {
            FileUtils.writeLines(logFile, processedLogs);
        } catch (IOException e) {
            throw new IllegalStateException("Error writing lines to log file");
        }
    }
}
