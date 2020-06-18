package org.goodiemania.melinoe.framework.web;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import org.goodiemania.melinoe.framework.Session;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriver implements ClosableDriver {
    private static final String RELOAD_FLAG_VALUE = "reloadFlag";

    private final Session session;
    private final FirefoxDriver webDriver;
    private final ScreenshotTaker screenshotTaker;
    private final WebDriverWait webDriverWait;
    private final LocalStorage localStorage;

    private String reloadFlag = "reloadFlag";

    public WebDriver(final Session session, final FirefoxDriver webDriver, final ScreenshotTaker screenshotTaker, final WebDriverWait webDriverWait, final LocalStorage localStorage) {
        this.session = session;
        this.webDriver = webDriver;
        this.screenshotTaker = screenshotTaker;
        this.webDriverWait = webDriverWait;
        this.localStorage = localStorage;
    }

    @Override
    public void close() {
        this.webDriver.close();;
    }

    public void get(final String url) {
        webDriver.get(url);
    }

    //TODO you shouldn't just be able to call this, it should be accessable by pages only...
    public void checkPage() {
        System.out.println("Checking page");
        webDriverWait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        //webLogger.addAndScreenshot("Checking page");

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
}
