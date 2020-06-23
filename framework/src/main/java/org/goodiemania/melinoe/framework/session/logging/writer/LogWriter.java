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
        logs.entrySet()
                .stream()
                .map(stringClassLoggerEntry -> {
                    final String color = stringClassLoggerEntry.getValue().getHasPassed() ? "#FF0000" : "";
                    return "\n"
                            + "        <div class=\"media text-muted pt-3\">\n"
                            + "            <svg class=\"bd-placeholder-img mr-2 rounded\" width=\"32\" height=\"32\" xmlns=\"http://www.w3.org/2000/svg\" preserveAspectRatio=\"xMidYMid slice\" focusable=\"false\" role=\"img\" aria-label=\"Placeholder: 32x32\"><title>Placeholder</title><rect width=\"100%\" height=\"100%\" fill=\"#007bff\"></rect><text x=\"50%\" y=\"50%\" fill=\"#007bff\" dy=\".3em\">32x32</text></svg>\n"
                            + "            <p class=\"media-body pb-3 mb-0 small lh-125 border-bottom border-gray\">\n"
                            + "                <strong class=\"d-block text-gray-dark\">@username</strong>\n"
                            + "                Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.\n"
                            + "            </p>\n"
                            + "        </div>"
                })

        template.addAll(1, Collections.emptyList());

        return Collections.emptyList();
    }
}
