package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;

/**
 * Created on 24/06/2019.
 */
public class LogFileManager {
    private final File runDirectory;
    private final File imageDir;
    private final File cssDir;

    public LogFileManager() {
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

            cssDir = new File(runDirectory, "css");
            FileUtils.forceMkdir(cssDir);



            System.out.println("Log URI: file://" + runDirectory.getAbsolutePath() + "/index.html");

        } catch (IOException e) {
            throw new MelinoeException(e);
        }
    }

    public File createRootLogFile() {
        try {
            File indexFile = new File(runDirectory, "index.html");
            FileUtils.touch(indexFile);

            return indexFile;
        } catch (final IOException e) {
            throw new MelinoeException(e);
        }
    }

    public File createCssFile() {
        try {
            File indexFile = new File(cssDir, "main.css");
            FileUtils.touch(indexFile);

            return indexFile;
        } catch (final IOException e) {
            throw new MelinoeException(e);
        }
    }

    public File createNewImageFile() {
        return new File(imageDir, UUID.randomUUID().toString() + ".png");
    }

    public File createLogFile(final String className) {
        return createLogFile(className, "index");
    }

    public File createLogFile(final String className, final String methodName) {
        try {
            final File testDirectory = new File(runDirectory, className);
            FileUtils.forceMkdir(runDirectory);
            File indexFile = new File(testDirectory, methodName + ".html");
            FileUtils.touch(indexFile);

            PrintWriter pw = new PrintWriter(indexFile);
            pw.close();

            return indexFile;
        } catch (final IOException e) {
            throw new MelinoeException(e);
        }
    }
}
