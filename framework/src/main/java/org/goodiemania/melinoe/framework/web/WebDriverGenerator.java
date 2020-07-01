package org.goodiemania.melinoe.framework.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.goodiemania.melinoe.framework.Session;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverGenerator {
    public WebDriverImpl generate(final Session session) {
        System.setProperty("webdriver.gecko.driver", waitForDriverExtraction("drivers/geckodriver.exe"));
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);
        options.setHeadless(true);

        //TODO find a better way to make this work
        File pathToFirefoxBinary = new File("C:\\Program Files\\Firefox Nightly\\firefox.exe");
        //File pathToFirefoxBinary = new File("C:\\Users\\thomasgo\\AppData\\Local\\Firefox Nightly\\firefox.exe");
        options.setBinary(pathToFirefoxBinary.getPath());

        FirefoxDriver webDriver = new FirefoxDriver(options);
        webDriver.manage().window().maximize();

        ScreenshotTaker screenshotTaker = new ScreenshotTaker(webDriver, webDriver);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
        LocalStorage localStorage = new LocalStorage(webDriver);

        return new WebDriverImpl(session, webDriver, screenshotTaker, webDriverWait, localStorage);
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
