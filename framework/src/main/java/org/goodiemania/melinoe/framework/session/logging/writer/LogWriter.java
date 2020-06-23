package org.goodiemania.melinoe.framework.session.logging.writer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.LogFileManager;

public class LogWriter {
    public void write(final Map<String, ClassLogger> logs) {
        List<String> templateStrings;
        try (InputStream resourceAsStream = LogWriter.class.getClassLoader().getResourceAsStream("html/logsTest.html")) {
            if (resourceAsStream == null) {
                throw new IllegalStateException("Log template came back null");
            }

            templateStrings = IOUtils.readLines(resourceAsStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        if (templateStrings.isEmpty()) {
            throw new IllegalStateException("Template is empty");
        }

        LogFileManager instance = LogFileManager.getInstance();
        File rootLogFile = instance.createRootLogFile();

        try {
            FileUtils.writeLines(rootLogFile, Collections.emptyList());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<String> createRootLogFile(final List<String> template, final Map<String, ClassLogger> logs) {
        logs.forEach((uniqueName, classLogger) -> {

        });

        template.addAll(1, Collections.emptyList());

        return Collections.emptyList();
    }
}
