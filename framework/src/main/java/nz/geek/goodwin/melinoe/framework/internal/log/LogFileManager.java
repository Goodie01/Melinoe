package nz.geek.goodwin.melinoe.framework.internal.log;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author Goodie
 */
public class LogFileManager {
    private final File runDirectory;
    private final File imageDir;
    private final File logFile;
    private final File logHtmlFile;

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

            logFile = new File(runDirectory, "log.json");
            FileUtils.touch(logFile);

            logHtmlFile = new File(runDirectory, "index.html");
            FileUtils.touch(logFile);

            System.out.println("Log URI: file:///" + logHtmlFile.getCanonicalPath().replace('\\','/'));

        } catch (IOException e) {
            throw new MelinoeException(e);
        }
    }

    public File getLogFile() {
        return logFile;
    }

    public File getLogHtmlFile() {
        return logHtmlFile;
    }

    public File getRunDirectory() { return runDirectory; }

    public File createNewImageFile() {
        return new File(imageDir, UUID.randomUUID().toString() + ".png");
    }
}
