package org.goodiemania.melinoe.framework.drivers.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.config.Configuration;
import org.goodiemania.melinoe.framework.session.MetaSession;
import org.goodiemania.melinoe.framework.session.logging.Logger;
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
    private MetaSession metaSession;

    private String reloadFlag = RELOAD_FLAG_VALUE;

    public RawWebDriver(final MetaSession metaSession, final Logger logger) {
        this.metaSession = metaSession;
        this.webDriver = new WebDriverImpl(logger, this);
    }

    public RemoteWebDriver getRemoteWebDriver() {
        if (remoteWebDriver == null) {
            generate();
        }

        return remoteWebDriver;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public ScreenshotTaker getScreenshotTaker() {
        return screenshotTaker;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public boolean hasPageBeenChecked() {
        return StringUtils.equals(localStorage.get(reloadFlag), RELOAD_FLAG_VALUE);
    }

    public void markPageChecked() {
        reloadFlag = UUID.randomUUID().toString();
        localStorage.put(reloadFlag, RELOAD_FLAG_VALUE);
    }

    private void generate() {
        final String browserChoice = Configuration.BROWSER.get();
        if (!StringUtils.equalsIgnoreCase(browserChoice, "FIREFOX")) {
            throw new MelinoeException(
                    String.format("Invalid browser choice selected, we currently only support 'firefox', '%s' was given", browserChoice)
            );
        }

        System.setProperty("webdriver.gecko.driver", waitForDriverExtraction("drivers/geckodriver.exe"));
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);
        options.setHeadless(true);

        options.setBinary(Configuration.BROWSER_EXE_LOCATION.get());

        this.remoteWebDriver = new FirefoxDriver(options);
        this.remoteWebDriver.manage().window().maximize();

        screenshotTaker = new ScreenshotTaker(metaSession, this);
        webDriverWait = new WebDriverWait(remoteWebDriver, Duration.ofSeconds(60));
        localStorage = new LocalStorage(this);

        metaSession.addDriver(webDriver);
    }

    private static String waitForDriverExtraction(final String driverLocation) {
        Optional<String> newDriverLocation;
        while ((newDriverLocation = attemptToExtractDriver(driverLocation)).isEmpty()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //suggestion from sonar
                Thread.currentThread().interrupt();
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

    public void close() {
        if (remoteWebDriver != null) {
            if (!hasPageBeenChecked()) {
                remoteWebDriver.quit();
                throw new MelinoeException("You must check your page before you close the Web Driver");
            }
            remoteWebDriver.quit();
            remoteWebDriver = null;
        }
    }
}
