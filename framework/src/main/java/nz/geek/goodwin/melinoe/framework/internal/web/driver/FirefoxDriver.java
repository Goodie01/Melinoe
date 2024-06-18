package nz.geek.goodwin.melinoe.framework.internal.web.driver;

import nz.geek.goodwin.melinoe.framework.internal.Configuration;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class FirefoxDriver {
    public static RemoteWebDriver get() {
        System.setProperty("webdriver.gecko.driver", CommonUtils.waitForDriverExtraction("drivers/geckodriver.exe"));
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);
        options.addArguments("-headless");

        options.setBinary(Configuration.BROWSER_EXE_LOCATION.val());

        RemoteWebDriver remoteWebDriver = new org.openqa.selenium.firefox.FirefoxDriver(options);
        remoteWebDriver.manage().window().maximize();

        return remoteWebDriver;
    }
}
