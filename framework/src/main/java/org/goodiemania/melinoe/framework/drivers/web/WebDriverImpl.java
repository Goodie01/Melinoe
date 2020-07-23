package org.goodiemania.melinoe.framework.drivers.web;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.goodiemania.melinoe.framework.api.web.By;
import org.goodiemania.melinoe.framework.api.web.Navigate;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.api.web.validators.WebValidator;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementImpl;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementListImpl;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;

public class WebDriverImpl implements ClosableDriver, WebDriver {
    private final Logger logger;
    private final RawWebDriver rawWebDriver;
    private final Navigate navigate;

    public WebDriverImpl(final Logger logger, final RawWebDriver rawWebDriver) {
        this.logger = logger;
        this.rawWebDriver = rawWebDriver;
        this.navigate = new NavigateImpl(logger, rawWebDriver);
    }

    @Override
    public void close() {
        this.rawWebDriver.close();
    }

    @Override
    public Navigate navigate() {
        return navigate;
    }

    @Override
    public String getTitle() {
        return rawWebDriver.getRemoteWebDriver().getTitle();
    }

    public void waitFor(final Predicate<WebDriver> predicate) {
        logger.add().withMessage("Waiting for predicate to be true");
        rawWebDriver.getWebDriverWait().until(webDriver -> predicate.test(this));
        logger.add().withMessage("Wait finished");
    }

    @Override
    public Optional<WebElement> findElement(final By by) {
        return Optional.of(new WebElementImpl(logger, rawWebDriver, by));
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return new WebElementListImpl(logger, rawWebDriver, by);
    }

    @Override
    public void checkPage(final List<WebValidator> validators) {
        rawWebDriver.markPageChecked();
        verify(validators);
    }

    @Override
    public void verify(final List<WebValidator> validators) {
        rawWebDriver.getWebDriverWait().until(givenWebDriver ->
                ((JavascriptExecutor) givenWebDriver).executeScript("return document.readyState").equals("complete"));

        logger.add()
                .withMessage("Checking page")
                .withImage(rawWebDriver.getScreenshotTaker().takeScreenshot());

        validators.stream()
                .map(webValidator -> webValidator.validate(rawWebDriver.getWebDriver()))
                .forEach(validationResult -> {
                    validationResult.getMessages().forEach(s -> {
                        if (validationResult.isValid()) {
                            logger.add()
                                    .withMessage(s);
                        } else {
                            logger.add()
                                    .withMessage(s)
                                    .fail();
                        }
                    });
                });

        if (!logger.getHasPassed()) {
            logger.add()
                    .withMessage("Failure in validation detected. Failing now.")
                    .fail();
            Assertions.fail();
        }
    }
}
