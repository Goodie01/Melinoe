package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created on 24/06/2019.
 */
public class LogFileManager {
    private static final LogFileManager INSTANCE = new LogFileManager();

    private final File runDirectory;
    private final File imageDir;

    private LogFileManager() {
        try {
            File baseDir = new File(System.getProperty("logDir", "logs"));
            FileUtils.forceMkdir(baseDir);

            String sessionName = System.getProperty("logName");
            if (StringUtils.isBlank(sessionName)) {
                sessionName = String.format("Test_%s_%s",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")),
                        UUID.randomUUID());
            }

            runDirectory = new File(baseDir, sessionName);
            FileUtils.forceMkdir(runDirectory);

            imageDir = new File(runDirectory, "img");
            FileUtils.forceMkdir(imageDir);

            System.out.println("Log URI: file://" + runDirectory.getAbsolutePath() + "/index.html");

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static LogFileManager getInstance() {
        return INSTANCE;
    }

    public File createLogFile(final String className) {
        try {
            final File testDirectory = new File(runDirectory, className);
            FileUtils.forceMkdir(runDirectory);
            File indexFile = new File(testDirectory, "index.html");
            FileUtils.touch(indexFile);

            return indexFile;
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public File createImageFile() {
        return new File(imageDir, UUID.randomUUID().toString() + ".jpg");
    }
}
