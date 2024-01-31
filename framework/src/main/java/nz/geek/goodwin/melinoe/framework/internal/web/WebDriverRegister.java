package nz.geek.goodwin.melinoe.framework.internal.web;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Goodie
 */
public class WebDriverRegister {
    private final List<WebDriver> webDrivers = new ArrayList<>();

    public void add(WebDriver webDriver) {
        webDrivers.add(webDriver);
    }

    public List<WebDriver> getWebDrivers() {
        return webDrivers;
    }
}
