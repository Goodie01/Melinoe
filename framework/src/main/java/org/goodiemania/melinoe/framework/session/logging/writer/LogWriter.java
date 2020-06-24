package org.goodiemania.melinoe.framework.session.logging.writer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.LogFileManager;

public class LogWriter {
    public void write(final Map<String, ClassLogger> logs) {
        LogFileManager instance = LogFileManager.getInstance();

        File rootLogFile = instance.createRootLogFile();
        
        logs.values().forEach(classLogger -> {
            //Add to root logging page...
            //add to class logging page...
            //write page....
            File logFile = instance.createLogFile(classLogger.getClassName(), classLogger.getMethodName());
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

            try {
                FileUtils.writeLines(logFile, processedLogs);
            } catch (IOException e) {
                throw new IllegalStateException("Error writing lines to log file");
            }
        });
    }
}
