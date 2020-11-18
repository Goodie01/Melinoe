package org.goodiemania.melinoe.framework.drivers.web.browsers;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class FirefoxHeadless {
    public static RemoteWebDriver get(final String browserExeLocation) {
        System.setProperty("webdriver.gecko.driver", CommonFox.waitForDriverExtraction("drivers/geckodriver.exe"));
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);
        options.setHeadless(true);

        options.setBinary(browserExeLocation);

        RemoteWebDriver remoteWebDriver = new FirefoxDriver(options);
        remoteWebDriver.manage().window().maximize();

        return remoteWebDriver;
    }
}
