package org.goodiemania.melinoe.framework.web;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.goodiemania.melinoe.framework.InternalSession;
import org.goodiemania.melinoe.framework.Session;
import org.goodiemania.melinoe.framework.web.validators.WebValidator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RawWebDriver {
    private static final String RELOAD_FLAG_VALUE = "reloadFlag";

    private RemoteWebDriver driver;
    private ScreenshotTaker screenshotTaker;
    private WebDriverWait webDriverWait;
    private LocalStorage localStorage;

    private WebDriverImpl webDriver;
    private InternalSession internalSession;

    private String reloadFlag = RELOAD_FLAG_VALUE;

    public RawWebDriver(final InternalSession internalSession) {
        this.internalSession = internalSession;
        this.webDriver = new WebDriverImpl(this);
    }

    public RemoteWebDriver remoteWebDriver() {
        if (driver == null) {
            generate();
        }

        return driver;
    }

    public WebDriverImpl getWebDriver() {
        return webDriver;
    }

    public void checkPage(final List<WebValidator> validators) {
        webDriverWait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        File file = screenshotTaker.takeScreenshot();
        internalSession.getSession().getLogger().add("Checking page", String.format("<img width='200' src='%s'>", "file:///" + file.getAbsolutePath()));

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

    //TODO not sure this should live here... but it doesn't need to be on the public web driver so
    public <T extends BasePage> T buildPage(final Class<T> classType) {
        try {
            return classType.getConstructor(Session.class).newInstance(internalSession.getSession());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private void generate() {
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

        this.driver = webDriver;

        //TODO find a beter way to handle this
        screenshotTaker = new ScreenshotTaker(this);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
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
