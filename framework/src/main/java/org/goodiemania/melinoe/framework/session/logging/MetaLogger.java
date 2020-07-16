package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MetaLogger {
    private final File rootLogFile;
    private final List<ClassLogger> classLoggers;
    private final LogFileManager fileManager;

    public MetaLogger() {
        fileManager = new LogFileManager();
        classLoggers = new ArrayList<>();

        rootLogFile = fileManager.createRootLogFile();
    }

    public ClassLogger createClassLogger(final String displayName, final String packageName, final String className) {
        ClassLogger classLogger = new ClassLogger(fileManager, displayName, packageName, className);
        classLoggers.add(classLogger);
        return classLogger;
    }

    public List<ClassLogger> getClassLoggers() {
        return classLoggers;
    }

    public File getRootLogFile() {
        return rootLogFile;
    }
}
