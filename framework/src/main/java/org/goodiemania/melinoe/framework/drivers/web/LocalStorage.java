package org.goodiemania.melinoe.framework.drivers.web;

import org.openqa.selenium.JavascriptException;

public class LocalStorage {
    private RawWebDriver rawWebDriver;

    public LocalStorage(final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
    }

    private void init() {
        try {
            rawWebDriver.getRemoteWebDriver().executeScript("return window.test.size;");
        } catch (JavascriptException e) {
            rawWebDriver.getRemoteWebDriver().executeScript("window.test = new Map();");
        }
    }

    public void put(String item, String value) {
        init();
        rawWebDriver.getRemoteWebDriver().executeScript(String.format(
                "window.test.set('%s','%s');", item, value));
    }

    public String get(String key) {
        init();
        return (String) rawWebDriver.getRemoteWebDriver().executeScript(String.format(
                "return window.test.get('%s');", key));
    }


    public Long size() {
        init();
        return (Long) rawWebDriver.getRemoteWebDriver().executeScript("return window.test.size;");
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
