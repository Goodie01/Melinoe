package org.goodiemania.melinoe.framework.web;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import org.goodiemania.melinoe.framework.Session;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.web.validators.WebValidator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriver implements ClosableDriver {
    private static final String RELOAD_FLAG_VALUE = "reloadFlag";

    private final Session session;
    private final org.openqa.selenium.WebDriver webDriver;
    private final ScreenshotTaker screenshotTaker;
    private final WebDriverWait webDriverWait;
    private final LocalStorage localStorage;

    private String reloadFlag = "reloadFlag";

    public WebDriver(final Session session, final org.openqa.selenium.WebDriver webDriver, final ScreenshotTaker screenshotTaker, final WebDriverWait webDriverWait, final LocalStorage localStorage) {
        this.session = session;
        this.webDriver = webDriver;
        this.screenshotTaker = screenshotTaker;
        this.webDriverWait = webDriverWait;
        this.localStorage = localStorage;
    }

    @Override
    public void close() {
        this.webDriver.close();
    }

    public void get(final String url) {
        webDriver.get(url);
    }

    //TODO you shouldn't just be able to call this, it should be accessable by pages only...
    public void checkPage(final List<WebValidator> validators) {
        webDriverWait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        File file = screenshotTaker.takeScreenshot();
        session.getLogger().add("Checking page", String.format("<img src='%s'>", file.getPath()));

        validators.stream()
                .map(webValidator -> webValidator.validate(session, webDriver))
                .forEach(validationResult -> {
                    if (!validationResult.isValid()) {
                        session.getLogger().fail();
                    }
                    validationResult.getMessages().forEach(s -> session.getLogger().add(s));
                });

        reloadFlag = UUID.randomUUID().toString();
        localStorage.put(reloadFlag, RELOAD_FLAG_VALUE);
    }

    public <T extends BasePage> T buildPage(final Class<T> classType) {
        try {
            return classType.getConstructor(Session.class).newInstance(session);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getTitle() {
        return webDriver.getTitle();
    }
}
