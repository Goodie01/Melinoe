package org.goodiemania.melinoe.framework.drivers.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.drivers.web.validators.WebValidator;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.session.MetaSession;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RawWebDriver {
    private static final String RELOAD_FLAG_VALUE = "reloadFlag";

    private RemoteWebDriver remoteWebDriver;
    private ScreenshotTaker screenshotTaker;
    private WebDriverWait webDriverWait;
    private LocalStorage localStorage;

    private WebDriverImpl webDriver;
    private InternalSession internalSession;

    private String reloadFlag = RELOAD_FLAG_VALUE;

    public RawWebDriver(final MetaSession metaSession, final InternalSession internalSession) {
        this.internalSession = internalSession;
        this.webDriver = new WebDriverImpl(internalSession, this);
        metaSession.addDriver(this.webDriver);
    }

    public RemoteWebDriver getRemoteWebDriver() {
        if (remoteWebDriver == null) {
            generate();
        }

        return remoteWebDriver;
    }

    public WebDriverImpl getWebDriver() {
        return webDriver;
    }

    public ScreenshotTaker getScreenshotTaker() {
        return screenshotTaker;
    }

    public void checkPage(final List<WebValidator> validators) {
        webDriverWait.until(givenWebDriver -> ((JavascriptExecutor) givenWebDriver).executeScript("return document.readyState").equals("complete"));

        internalSession.getSession().getLogger().addWithImage("Checking page", screenshotTaker.takeScreenshot());

        validators.stream()
                .map(webValidator -> webValidator.validate(internalSession.getSession(), getWebDriver()))
                .forEach(validationResult -> {
                    if (!validationResult.isValid()) {
                        internalSession.getSession().getLogger().fail();
                    }
                    validationResult.getMessages().forEach(s -> internalSession.getSession().getLogger().add(s));
                });

        reloadFlag = UUID.randomUUID().toString();
        localStorage.put(reloadFlag, RELOAD_FLAG_VALUE);
    }

    public boolean hasPageBeenChecked() {
        return StringUtils.equals(localStorage.get(reloadFlag), RELOAD_FLAG_VALUE);
    }

    private void generate() {
        System.setProperty("webdriver.gecko.driver", waitForDriverExtraction("drivers/geckodriver.exe"));
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);
        options.setHeadless(true);

        File pathToFirefoxBinary = new File("C:\\Program Files\\Firefox Nightly\\firefox.exe");
        options.setBinary(pathToFirefoxBinary.getPath());

        this.remoteWebDriver = new FirefoxDriver(options);
        this.remoteWebDriver.manage().window().maximize();

        screenshotTaker = new ScreenshotTaker(this);
        webDriverWait = new WebDriverWait(remoteWebDriver, Duration.ofSeconds(60));
        localStorage = new LocalStorage(this);
    }

    private static String waitForDriverExtraction(final String driverLocation) {
        Optional<String> newDriverLocation;
        while ((newDriverLocation = attemptToExtractDriver(driverLocation)).isEmpty()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //suggestion from sonar
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
        return newDriverLocation.get();
    }

    private static Optional<String> attemptToExtractDriver(final String driverLocation) {
        File targetFile = new File("target/" + driverLocation);

        if (!targetFile.exists()) {
            InputStream resourceAsStream = WebDriverImpl.class.getClassLoader().getResourceAsStream(driverLocation);
            Objects.requireNonNull(resourceAsStream);
            try {
                FileUtils.copyInputStreamToFile(resourceAsStream, targetFile);
            } catch (IOException e) {
                return Optional.empty();
            }
        }

        return Optional.of(targetFile.getAbsolutePath());
    }
}