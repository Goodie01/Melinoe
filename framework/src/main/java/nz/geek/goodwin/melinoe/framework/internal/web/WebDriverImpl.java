package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.Cookie;
import nz.geek.goodwin.melinoe.framework.api.web.Navigate;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;
import nz.geek.goodwin.melinoe.framework.internal.Configuration;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import nz.geek.goodwin.melinoe.framework.internal.misc.Sleeper;
import nz.geek.goodwin.melinoe.framework.internal.verify.VerificationUtils;
import nz.geek.goodwin.melinoe.framework.internal.web.decorator.FlowDecorator;
import nz.geek.goodwin.melinoe.framework.internal.web.driver.FirefoxDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    public WebDriverImpl(Session session, LogFileManager logFileManager, Logger logger, ClosableRegister closableRegister) {
        this.logFileManager = logFileManager;
        this.logger = logger;

        if(StringUtils.equalsIgnoreCase("Firefox", Configuration.BROWSER.val())) {
            this.remoteWebDriver = FirefoxDriver.get();
        } else {
            throw new IllegalArgumentException("Invalid Browser choice selected");
        }
        closableRegister.add(this.remoteWebDriver);

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
    public void clearCookie(String name) {
        remoteWebDriver.manage().getCookieNamed(name);
    }

    @Override
    public void clearCookie(Cookie cookie) {
        remoteWebDriver.manage().getCookieNamed(cookie.getName());
    }

    @Override
    public void clearAll() {
        remoteWebDriver.manage().deleteAllCookies();
    }

    @Override
    public Map<String, Cookie> cookies() {
        if(!pageCheckStatus.check()) {
            throw new MelinoeException("Please check run at least one validator before interacting with a pages cookies.");
        }

        return remoteWebDriver.manage().getCookies()
                .stream()
                .map(CookieImpl::new)
                .collect(Collectors.toMap(CookieImpl::getName, Function.identity()));
    }

    @Override
    public void verify(List<WebValidator> validators) {
        waitFor(webDriver -> remoteWebDriver.executeScript("return document.readyState").equals("complete"));
        VerificationUtils.validate(validators, logger.createSublogger("Verifying page - " + getTitle(), screenshotTaker.takeScreenshot()), () -> this);
        pageCheckStatus.set();
    }
}
