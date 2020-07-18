package org.goodiemania.melinoe.framework.session.logging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MetaLogger {
    private final File rootLogFile;
    private final List<ClassLogger> classLoggers;
    private final LogFileManager fileManager;
    private final File cssFile;

    public MetaLogger() {
        fileManager = new LogFileManager();
        classLoggers = new ArrayList<>();

        rootLogFile = fileManager.createRootLogFile();
        cssFile = fileManager.createCssFile();
    }

    public List<ClassLogger> getClassLoggers() {
        return classLoggers;
    }

    public File getRootLogFile() {
        return rootLogFile;
    }

    public ClassLogger createClassLogger(final ExtensionContext extensionContext) {
        String displayName = extensionContext.getDisplayName();

        String packageName = extensionContext.getTestClass()
                .map(Class::getPackageName)
                .orElse("NO_PACKAGE");

        String className = extensionContext.getTestClass()
                .map(Class::getName)
                .orElse("NO_CLASS_NAME");

        ClassLogger classLogger = new ClassLogger(fileManager, displayName, packageName, className);
        classLoggers.add(classLogger);
        return classLogger;
    }

    public File getCssFile() {
        return cssFile;
    }
}
