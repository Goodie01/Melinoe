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

public class WebDriverImpl implements ClosableDriver, WebDriver {
    private static final String RELOAD_FLAG_VALUE = "reloadFlag";

    private final Session session;
    private final org.openqa.selenium.WebDriver webDriver;
    private final ScreenshotTaker screenshotTaker;
    private final WebDriverWait webDriverWait;
    private final LocalStorage localStorage;

    private String reloadFlag = RELOAD_FLAG_VALUE;
    private NavigateImpl navigate;

    public WebDriverImpl(final Session session,
                         final org.openqa.selenium.WebDriver webDriver,
                         final ScreenshotTaker screenshotTaker,
                         final WebDriverWait webDriverWait,
                         final LocalStorage localStorage) {
        this.session = session;
        this.webDriver = webDriver;
        this.screenshotTaker = screenshotTaker;
        this.webDriverWait = webDriverWait;
        this.localStorage = localStorage;

        this.navigate = new NavigateImpl(webDriver);
    }

    @Override
    public void close() {
        this.webDriver.close();
    }

    @Override
    public Navigate navigate() {
        return navigate;
    }

    //TODO Should anyone be able to call this?
    @Override
    public void checkPage(final List<WebValidator> validators) {
        webDriverWait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        File file = screenshotTaker.takeScreenshot();
        session.getLogger().add("Checking page", String.format("<img width='200' src='%s'>", "file:///" + file.getAbsolutePath()));

        validators.stream()
                .map(webValidator -> webValidator.validate(session, this))
                .forEach(validationResult -> {
                    if (!validationResult.isValid()) {
                        session.getLogger().fail();
                    }
                    validationResult.getMessages().forEach(s -> session.getLogger().add(s));
                });

        reloadFlag = UUID.randomUUID().toString();
        localStorage.put(reloadFlag, RELOAD_FLAG_VALUE);
    }

    //TODO should this be publicly accessible?
    public <T extends BasePage> T buildPage(final Class<T> classType) {
        try {
            return classType.getConstructor(Session.class).newInstance(session);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getTitle() {
        return webDriver.getTitle();
    }
}
