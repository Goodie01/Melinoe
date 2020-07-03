package org.goodiemania.melinoe.framework.web;

import org.openqa.selenium.JavascriptException;

public class LocalStorage {
    private RawWebDriver rawWebDriver;

    public LocalStorage(final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
    }

    private void init() {
        try {
            rawWebDriver.remoteWebDriver().executeScript("return window.test.size;");
        } catch (JavascriptException e) {
            rawWebDriver.remoteWebDriver().executeScript("window.test = new Map();");
        }
    }

    public void put(String item, String value) {
        init();
        rawWebDriver.remoteWebDriver().executeScript(String.format(
                "window.test.set('%s','%s');", item, value));
    }

    public String get(String key) {
        init();
        return (String) rawWebDriver.remoteWebDriver().executeScript(String.format(
                "return window.test.get('%s');", key));
    }


    public Long size() {
        init();
        return (Long) rawWebDriver.remoteWebDriver().executeScript("return window.test.size;");
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
