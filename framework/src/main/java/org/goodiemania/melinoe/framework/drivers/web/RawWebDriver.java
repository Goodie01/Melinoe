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
import org.goodiemania.melinoe.framework.drivers.web.browsers.DriverChooser;
import org.goodiemania.melinoe.framework.drivers.web.browsers.FirefoxHeadless;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RawWebDriver {
    private static final String RELOAD_FLAG_VALUE = "reloadFlag";

    private final InternalSession internalSession;

    private RemoteWebDriver remoteWebDriver;
    private ScreenshotTaker screenshotTaker;
    private WebDriverWait webDriverWait;
    private LocalStorage localStorage;

    private final WebDriverImpl webDriver;

    private String reloadFlag = RELOAD_FLAG_VALUE;

    public RawWebDriver(final InternalSession internalSession) {
        this.internalSession = internalSession;
        this.webDriver = new WebDriverImpl(internalSession);
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
        if (remoteWebDriver == null) {
            generate();
        }

        return StringUtils.equals(localStorage.get(reloadFlag), RELOAD_FLAG_VALUE);
    }

    public void markPageChecked() {
        if (remoteWebDriver == null) {
            generate();
        }

        reloadFlag = UUID.randomUUID().toString();
        localStorage.put(reloadFlag, RELOAD_FLAG_VALUE);
    }

    private void generate() {
        final String browserChoice = Configuration.BROWSER.get();

        this.remoteWebDriver = DriverChooser.chooseDriver(browserChoice)
                .apply(Configuration.BROWSER_EXE_LOCATION.get());

        this.remoteWebDriver.manage().window().maximize();

        screenshotTaker = new ScreenshotTaker(internalSession);
        webDriverWait = new WebDriverWait(remoteWebDriver, Duration.ofSeconds(60));
        localStorage = new LocalStorage(this);

        internalSession.getMetaSession().addDriver(webDriver);
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
