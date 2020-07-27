package org.goodiemania.melinoe.framework.drivers.web.page;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.web.By;
import org.goodiemania.melinoe.framework.api.web.ConvertMelinoeBy;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebElementImpl implements WebElement {
    private final InternalSession internalSession;
    private final Function<RemoteWebDriver, org.openqa.selenium.WebElement> webElementSupplier;

    public WebElementImpl(final InternalSession internalSession,
                          final Function<RemoteWebDriver, org.openqa.selenium.WebElement> webElementSupplier) {
        this.internalSession = internalSession;
        this.webElementSupplier = webElementSupplier;
    }

    public WebElementImpl(final InternalSession internalSession,
                          final By by) {
        this(internalSession, remoteWebDriver -> ConvertMelinoeBy.build(by).findElement(remoteWebDriver));
    }

    private void withElement(final TriWebConsumer function) {
        function.accept(getElement(), getDriver(), internalSession.getLogger());
    }

    private org.openqa.selenium.WebElement getElement() {
        return webElementSupplier.apply(getDriver().getRemoteWebDriver());
    }

    private RawWebDriver getDriver() {
        if (!internalSession.getRawWebDriver().hasPageBeenChecked()) {
            throw new MelinoeException("Please check page before interacting with it");
        }

        return internalSession.getRawWebDriver();
    }

    @Override
    public void click() {
        withElement((webElement, rawWebDriver, logger) -> {
            File file = rawWebDriver.getScreenshotTaker().takeScreenshot(webElement);
            logger.add()
                    .withMessage("Clicking link")
                    .withImage(file);
            webElement.click();
        });

    }

    @Override
    public void submit() {
        withElement((webElement, rawWebDriver, logger) -> {
            File file = rawWebDriver.getScreenshotTaker().takeScreenshot(webElement);
            logger.add()
                    .withMessage("Submitted element")
                    .withImage(file);
            webElement.submit();
        });
    }

    @Override
    public void sendKeys(final String keysToSend) {
        withElement((webElement, rawWebDriver, logger) -> {
            File file = rawWebDriver.getScreenshotTaker().takeScreenshot(webElement);
            logger.add()
                    .withMessage(String.format("Entering text '%s' into element",
                            keysToSend))
                    .withImage(file);
            webElement.sendKeys(keysToSend);
        });
    }

    @Override
    public void clear() {
        withElement((webElement, rawWebDriver, logger) -> {
            File file = rawWebDriver.getScreenshotTaker().takeScreenshot(webElement);
            logger.add()
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
        org.openqa.selenium.WebElement seleniumWebElement = this.webElementSupplier.apply(getDriver().getRemoteWebDriver());

        return new WebElementListImpl(internalSession,
                remoteWebDriver -> ConvertMelinoeBy.build(by).findElements(seleniumWebElement));
    }

    @Override
    public Optional<WebElement> findElement(final By by) {
        org.openqa.selenium.WebElement seleniumWebElement = this.webElementSupplier.apply(getDriver().getRemoteWebDriver());

        return Optional.of(
                new WebElementImpl(internalSession,
                        remoteWebDriver -> ConvertMelinoeBy.build(by).findElement(seleniumWebElement)));
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

    private interface TriWebConsumer {
        void accept(org.openqa.selenium.WebElement webElement, RawWebDriver rawWebDriver, Logger logger);
    }
}
