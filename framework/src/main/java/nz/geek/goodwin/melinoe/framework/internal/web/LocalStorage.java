package nz.geek.goodwin.melinoe.framework.internal.web;

import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Goodie
 */
public class LocalStorage {
    private final RemoteWebDriver remoteWebDriver;

    public LocalStorage(final RemoteWebDriver remoteWebDriver) {
        this.remoteWebDriver = remoteWebDriver;
    }

    private void init() {
        try {
            remoteWebDriver.executeScript("return window.test.size;");
        } catch (JavascriptException e) {
            remoteWebDriver.executeScript("window.test = new Map();");
        }
    }

    public void put(String item, String value) {
        init();
        remoteWebDriver.executeScript(String.format(
                "window.test.set('%s','%s');", item, value));
    }

    public String get(String key) {
        init();
        return (String) remoteWebDriver.executeScript(String.format(
                "return window.test.get('%s');", key));
    }


    public Long size() {
        init();
        return (Long) remoteWebDriver.executeScript("return window.test.size;");
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
