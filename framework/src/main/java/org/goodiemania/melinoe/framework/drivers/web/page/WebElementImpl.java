package org.goodiemania.melinoe.framework.drivers.web.page;

import java.io.File;
import java.util.function.Consumer;
import org.goodiemania.melinoe.framework.api.WebElement;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebElementImpl implements WebElement {
    private InternalSession internalSession;
    private By by;

    public WebElementImpl(final InternalSession internalSession, final By by) {
        this.internalSession = internalSession;
        this.by = by;
    }

    private RemoteWebDriver getDriver() {
        if (!internalSession.getRawWebDriver().hasPageBeenChecked()) {
            throw new IllegalStateException("Please check page before interacting with it");
        }

        return internalSession.getRawWebDriver().getRemoteWebDriver();
    }

    private void withDriver(final Consumer<org.openqa.selenium.WebElement> function) {
        if (!internalSession.getRawWebDriver().hasPageBeenChecked()) {
            throw new IllegalStateException("Please check page before interacting with it");
        }

        function.accept(internalSession.getRawWebDriver().getRemoteWebDriver().findElement(by));
    }

    @Override
    public void click() {
        withDriver(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().addWithImage(String.format("Clicking link found by: %s", by), file);
            webElement.click();
        });

    }

    @Override
    public void submit() {
        withDriver(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().addWithImage(String.format("Submitted element found by: %s", by), file);
            webElement.submit();
        });
    }

    @Override
    public void sendKeys(final String keysToSend) {
        withDriver(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().addWithImage(
                    String.format("Entering text '%s' into element found by: %s",
                            keysToSend,
                            by),
                    file);
            webElement.sendKeys(keysToSend);
        });
    }

    @Override
    public void clear() {
        withDriver(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().addWithImage(
                    String.format("Calling clear on element found by: %s",
                            by),
                    file);
            webElement.clear();
        });
    }

    @Override
    public String getTagName() {
        return getDriver().findElement(by).getTagName();
    }

    @Override
    public String getAttribute(final String name) {
        return getDriver().findElement(by).getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return getDriver().findElement(by).isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getDriver().findElement(by).isEnabled();
    }

    @Override
    public String getText() {
        return getDriver().findElement(by).getText();
    }

    @Override
    public boolean isDisplayed() {
        return getDriver().findElement(by).isDisplayed();
    }

    @Override
    public Point getLocation() {
        return getDriver().findElement(by).getLocation();
    }

    @Override
    public Dimension getSize() {
        return getDriver().findElement(by).getSize();
    }

    @Override
    public Rectangle getRect() {
        return getDriver().findElement(by).getRect();
    }
}
