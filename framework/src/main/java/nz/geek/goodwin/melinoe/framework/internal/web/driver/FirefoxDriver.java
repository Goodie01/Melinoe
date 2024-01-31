package nz.geek.goodwin.melinoe.framework.internal.web.driver;

import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class FirefoxDriver {
    public static RemoteWebDriver get(final String browserExeLocation) {
        System.setProperty("webdriver.gecko.driver", CommonUtils.waitForDriverExtraction("geckodriver.exe"));
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);
        options.addArguments("-headless");

        options.setBinary(browserExeLocation);

        RemoteWebDriver remoteWebDriver = new org.openqa.selenium.firefox.FirefoxDriver(options);
        remoteWebDriver.manage().window().maximize();

        return remoteWebDriver;
    }
}
