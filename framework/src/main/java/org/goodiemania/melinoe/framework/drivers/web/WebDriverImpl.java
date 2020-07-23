package org.goodiemania.melinoe.framework.drivers.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import net.bytebuddy.implementation.bytecode.assign.reference.ReferenceTypeAwareAssigner;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.web.By;
import org.goodiemania.melinoe.framework.api.web.Navigate;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.api.web.validators.WebValidator;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementImpl;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementListImpl;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;

public class WebDriverImpl implements ClosableDriver, WebDriver {
    private InternalSession internalSession;
    private final RawWebDriver rawWebDriver;

    private Navigate navigate;

    public WebDriverImpl(final InternalSession internalSession, final RawWebDriver rawWebDriver) {
        this.internalSession = internalSession;
        this.rawWebDriver = rawWebDriver;
        this.navigate = new NavigateImpl(internalSession, rawWebDriver);
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
        internalSession.getSession().getLogger().add().withMessage("Waiting for predicate to be true");
        rawWebDriver.getWebDriverWait().until(webDriver -> predicate.test(this));
        internalSession.getSession().getLogger().add().withMessage("Wait finished");
    }

    @Override
    public Optional<WebElement> findElement(final By by) {
        return Optional.of(new WebElementImpl(internalSession, by));
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return new WebElementListImpl(internalSession, by);
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

        Session session = internalSession.getSession();
        session.getLogger().add()
                .withMessage("Checking page")
                .withImage(rawWebDriver.getScreenshotTaker().takeScreenshot());

        validators.stream()
                .map(webValidator -> webValidator.validate(session, rawWebDriver.getWebDriver()))
                .forEach(validationResult -> {
                    validationResult.getMessages().forEach(s -> {
                        if (validationResult.isValid()) {
                            session.getLogger().add()
                                    .withMessage(s);
                        } else {
                            session.getLogger().add()
                                    .withMessage(s)
                                    .fail();
                        }
                    });
                });

        if (!session.getLogger().getHasPassed()) {
            session.getLogger().add()
                    .withMessage("Failure in validation detected. Failing now.")
                    .fail();
            Assertions.fail();
        }
    }
}
