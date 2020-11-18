package org.goodiemania.melinoe.framework.drivers.web;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.goodiemania.melinoe.framework.api.web.By;
import org.goodiemania.melinoe.framework.api.web.Navigate;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.api.web.validators.WebValidator;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementImpl;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementListImpl;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.validators.Validator;
import org.openqa.selenium.JavascriptExecutor;

public class WebDriverImpl implements WebDriver {
    private final InternalSession internalSession;
    private final Navigate navigate;

    public WebDriverImpl(final InternalSession internalSession) {
        this.internalSession = internalSession;
        this.navigate = new NavigateImpl(internalSession);
    }

    @Override
    public void close() {
        this.internalSession.getRawWebDriver().close();
    }

    @Override
    public Navigate navigate() {
        return navigate;
    }

    @Override
    public String getTitle() {
        return internalSession.getRawWebDriver().getRemoteWebDriver().getTitle();
    }

    public void waitFor(final Predicate<WebDriver> predicate) {
        internalSession.getLogger().add().withMessage("Waiting for predicate to be true");
        internalSession.getRawWebDriver().getWebDriverWait().until(webDriver -> predicate.test(this));
        internalSession.getLogger().add().withMessage("Wait finished");
    }

    @Override
    public WebElement findElement(final By by) {
        return new WebElementImpl(internalSession, by);
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return new WebElementListImpl(internalSession, by);
    }

    @Override
    public void checkPage(final List<WebValidator> validators) {
        internalSession.getLogger().add()
                .withMessage("Checking page")
                .withImage(internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot());

        internalSession.getRawWebDriver().getWebDriverWait().until(givenWebDriver ->
                ((JavascriptExecutor) givenWebDriver).executeScript("return document.readyState").equals("complete"));

        internalSession.getRawWebDriver().markPageChecked();

        final List<Validator> collect = validators.stream()
                .map(webValidator -> (Validator) () -> webValidator.validate(internalSession.getRawWebDriver().getWebDriver()))
                .collect(Collectors.toList());

        internalSession.getSession().verify(collect);
    }

    @Override
    public void verify(final List<WebValidator> validators) {
        internalSession.getSession().getLogger().add().withMessage("Validating web page")
                .withImage(internalSession.getRawWebDriver().getScreenshotTaker().takeScreenshot());

        internalSession.getRawWebDriver().getWebDriverWait().until(givenWebDriver ->
                ((JavascriptExecutor) givenWebDriver).executeScript("return document.readyState").equals("complete"));

        final List<Validator> collect = validators.stream()
                .map(webValidator -> (Validator) () -> webValidator.validate(internalSession.getRawWebDriver().getWebDriver()))
                .collect(Collectors.toList());

        internalSession.getSession().verify(collect);
    }
}
