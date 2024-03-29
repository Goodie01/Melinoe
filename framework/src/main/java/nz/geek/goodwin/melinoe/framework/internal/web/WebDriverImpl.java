package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.log.LogMessage;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.Navigate;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;
import nz.geek.goodwin.melinoe.framework.internal.Configuration;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import nz.geek.goodwin.melinoe.framework.internal.misc.Sleeper;
import nz.geek.goodwin.melinoe.framework.internal.web.decorator.FlowDecorator;
import nz.geek.goodwin.melinoe.framework.internal.web.driver.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Goodie
 */
public class WebDriverImpl implements WebDriver {

    private final RemoteWebDriver remoteWebDriver;
    private final ScreenshotTaker screenshotTaker;
    private final LogFileManager logFileManager;
    private final Logger logger;
    private final FlowDecorator flowDecorator;
    private final PageCheckStatus pageCheckStatus;

    public WebDriverImpl(Session session, LogFileManager logFileManager, Logger logger, WebDriverRegister webDriverRegister) {
        this.logFileManager = logFileManager;
        this.logger = logger;

        this.remoteWebDriver = FirefoxDriver.get("C:\\Program Files\\Firefox Nightly\\firefox.exe");
        webDriverRegister.add(this.remoteWebDriver);

        this.screenshotTaker = new ScreenshotTaker(remoteWebDriver, logFileManager);
        this.flowDecorator = new FlowDecorator(session);
        this.pageCheckStatus = new PageCheckStatus(remoteWebDriver);
    }

    @Override
    public void decorate(final Object object) {
        this.flowDecorator.decorate(object);
    }

    @Override
    public void decorateClass(final Class<?> classType) {
        this.flowDecorator.decorateClass(classType);
    }

    @Override
    public Navigate navigate() {
        return new NavigateImpl(remoteWebDriver, logger);
    }

    @Override
    public String getTitle() {
        return remoteWebDriver.getTitle();
    }

    public void takeScreenshot() {
        logFileManager.getLogFile();
        logger.add().withMessage("Screenshot").withImage(screenshotTaker.takeScreenshot());
    }
    public void takeScreenshot(final String logMessage) {
        logger.add().withMessage(logMessage).withImage(screenshotTaker.takeScreenshot());
    }

    @Override
    public void waitFor(Predicate<WebDriver> predicate) {
        Instant end = Instant.now().plus(Duration.ofSeconds(30));

        while(true) {
            Throwable lastException;
            try {
                boolean test = predicate.test(this);
                if (test) {
                    return;
                }

                lastException = null;
            } catch (Exception e) {
                lastException = e;
            }

            if (end.isBefore(Instant.now())) {
                throw new RuntimeException("Ran out of time", lastException);
            }

            try {
                Thread.sleep(Duration.ofMillis(500));
            } catch (InterruptedException var6) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(var6);
            }
        }
    }

    @Override
    public WebElement findElement(By by) {
        return new WebElementImpl(remoteWebDriver, screenshotTaker, pageCheckStatus, logger, by);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return new WebElementListImpl(remoteWebDriver, screenshotTaker, pageCheckStatus, logger, by);
    }

    @Override
    public void verify(List<WebValidator> validators) {
        if(!pageCheckStatus.check()) {
            logger.add().withMessage("Verifying page").withImage(screenshotTaker.takeScreenshot());
        }

        waitFor(webDriver -> remoteWebDriver.executeScript("return document.readyState").equals("complete"));

        List<ValidationResult> list;
        int retryCount = 0;

        do {
            retryCount++;
            list = validators.stream()
                    .map(webValidator -> webValidator.validate(this))
                    .toList();
            Sleeper.sleep(Configuration.RETRY_SLEEP_TIME_MS.intVal());
        } while (list.stream().anyMatch(validationResult -> !validationResult.valid()) && retryCount < Configuration.RETRY_COUNT.intVal());

        final int tryCount = retryCount;

        list.forEach(validationResult -> {
            String messages = validationResult.messages().stream().collect(Collectors.joining(System.lineSeparator()));
            if(tryCount != 1) {
                messages = messages + System.lineSeparator() + "Took " + tryCount + " attempts to achieve result";
            }
            logger.add().withMessage(messages).withSuccess(validationResult.valid());
        });

        List<ValidationResult> list1 = list.stream()
                .filter(validationResult -> !validationResult.valid())
                .toList();

        if(!list1.isEmpty()) {
            throw new MelinoeException("Verification failed");
        }

        pageCheckStatus.set();
    }
}
