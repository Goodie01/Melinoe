package org.goodiemania.melinoe.framework.drivers.web.page;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebElementImpl implements WebElement {
    private final InternalSession internalSession;
    private final Function<RemoteWebDriver, org.openqa.selenium.WebElement> webElementSupplier;

    public WebElementImpl(final InternalSession internalSession, final Function<RemoteWebDriver, org.openqa.selenium.WebElement> webElementSupplier) {
        this.internalSession = internalSession;
        this.webElementSupplier = webElementSupplier;
    }

    public WebElementImpl(final InternalSession internalSession, final By by) {
        this(internalSession, by::findElement);
    }

    private void withElement(final Consumer<org.openqa.selenium.WebElement> function) {
        function.accept(getElement());
    }

    private org.openqa.selenium.WebElement getElement() {
        return webElementSupplier.apply(getDriver());
    }

    private RemoteWebDriver getDriver() {
        if (!internalSession.getRawWebDriver().hasPageBeenChecked()) {
            throw new MelinoeException("Please check page before interacting with it");
        }

        return internalSession.getRawWebDriver().getRemoteWebDriver();
    }

    @Override
    public void click() {
        withElement(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().add()
                    .withMessage("Clicking link")
                    .withImage(file);
            webElement.click();
        });

    }

    @Override
    public void submit() {
        withElement(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().add()
                    .withMessage("Submitted element")
                    .withImage(file);
            webElement.submit();
        });
    }

    @Override
    public void sendKeys(final String keysToSend) {
        withElement(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().add()
                    .withMessage(String.format("Entering text '%s' into element",
                            keysToSend))
                    .withImage(file);
            webElement.sendKeys(keysToSend);
        });
    }

    @Override
    public void clear() {
        withElement(webElement -> {
            File file = internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot(webElement);
            internalSession.getSession().getLogger().add()
                    .withMessage("Calling clear on element")
                    .withImage(file);
            webElement.clear();
        });
    }

    @Override
    public String getTagName() {
        return getElement().getTagName();
    }

    @Override
    public String getAttribute(final String name) {
        return getElement().getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return getElement().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getElement().isEnabled();
    }

    @Override
    public String getText() {
        return getElement().getText();
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return Collections.emptyList();
    }

    @Override
    public Optional<WebElement> findElement(final By by) {
        org.openqa.selenium.WebElement seleniumWebElement = this.webElementSupplier.apply(internalSession.getRawWebDriver().getRemoteWebDriver());

        return Optional.of(new WebElementImpl(internalSession, remoteWebDriver -> by.findElement(seleniumWebElement)));
    }

    @Override
    public boolean isDisplayed() {
        return getElement().isDisplayed();
    }

    @Override
    public Point getLocation() {
        return getElement().getLocation();
    }

    @Override
    public Dimension getSize() {
        return getElement().getSize();
    }

    @Override
    public Rectangle getRect() {
        return getElement().getRect();
    }

    @Override
    public String getCssValue(final String propertyName) {
        return null;
    }
}
