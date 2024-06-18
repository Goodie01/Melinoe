package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Goodie
 */
public class WebElementImpl implements WebElement {
    private final Function<RemoteWebDriver, org.openqa.selenium.WebElement> webElementSupplier;
    private final By by;
    private final RemoteWebDriver remoteWebDriver;
    private final ScreenshotTaker screenshotTaker;
    private final PageCheckStatus pageCheckStatus;
    private final Logger logger;

    public WebElementImpl(RemoteWebDriver remoteWebDriver, ScreenshotTaker screenshotTaker, PageCheckStatus pageCheckStatus, Logger logger, By by, Function<RemoteWebDriver, org.openqa.selenium.WebElement> parentSupplier) {
        this.remoteWebDriver = remoteWebDriver;
        this.screenshotTaker = screenshotTaker;
        this.pageCheckStatus = pageCheckStatus;
        this.logger = logger;
        this.by = by;

        this.webElementSupplier = (webDriver) -> parentSupplier.apply(webDriver).findElement(ConvertBy.build(by));
    }

    public WebElementImpl(RemoteWebDriver remoteWebDriver, ScreenshotTaker screenshotTaker, PageCheckStatus pageCheckStatus, Logger logger, By by) {
        this.remoteWebDriver = remoteWebDriver;
        this.screenshotTaker = screenshotTaker;
        this.pageCheckStatus = pageCheckStatus;
        this.logger = logger;
        this.by = by;

        this.webElementSupplier = (webDriver) -> webDriver.findElement(ConvertBy.build(by));
    }

    private void withElement(final Consumer<org.openqa.selenium.WebElement> function) {
        org.openqa.selenium.WebElement foundWebElement = webElementSupplier.apply(remoteWebDriver);
        function.accept(foundWebElement);
    }
    private void loggedActionWithElement(final String text, final Consumer<org.openqa.selenium.WebElement> function) {
        if(!pageCheckStatus.check()) {
            throw new MelinoeException("Please check run at least one validator before interacting with a page.");
        }
        withElement(webElement -> {
            String file = screenshotTaker.takeScreenshot(webElement);
            logger.add().withMessage(text + " element identified by " + by.toString()).withImage(file);
            function.accept(webElement);
        });
    }

    @Override
    public void hover() {
        loggedActionWithElement("Hovering over ", webElement -> new Actions(remoteWebDriver).moveToElement(webElement).perform());
    }

    @Override
    public void click() {
        loggedActionWithElement("Clicking", org.openqa.selenium.WebElement::click);
    }

    @Override
    public void submit() {
        loggedActionWithElement("Submitting", org.openqa.selenium.WebElement::submit);
    }

    @Override
    public void sendKeys(String stringToEnter) {
        loggedActionWithElement("Sending Keys to", webElement -> webElement.sendKeys(stringToEnter));
    }

    @Override
    public void clear() {
        loggedActionWithElement("Clearing", org.openqa.selenium.WebElement::clear);
    }

    @Override
    public String getTagName() {
        return webElementSupplier.apply(remoteWebDriver).getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return webElementSupplier.apply(remoteWebDriver).getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return webElementSupplier.apply(remoteWebDriver).isSelected();
    }

    @Override
    public boolean isEnabled() {
        return webElementSupplier.apply(remoteWebDriver).isEnabled();
    }

    @Override
    public String getText() {
        return webElementSupplier.apply(remoteWebDriver).getText();
    }

    @Override
    public boolean isDisplayed() {
        return webElementSupplier.apply(remoteWebDriver).isDisplayed();
    }

    @Override
    public String getCssValue(String propertyName) {
        return webElementSupplier.apply(remoteWebDriver).getCssValue(propertyName);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return new WebElementListImpl(remoteWebDriver, screenshotTaker, pageCheckStatus, logger, webElementSupplier, by);
    }

    @Override
    public WebElement findElement(By by) {
        return new WebElementImpl(remoteWebDriver, screenshotTaker, pageCheckStatus, logger, by, webElementSupplier);
    }
}
