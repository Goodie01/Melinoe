package org.goodiemania.melinoe.framework.web;

import org.goodiemania.melinoe.framework.Session;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;

public class LocalStorage {
    private JavascriptExecutor jsExecutor;

    public LocalStorage(final JavascriptExecutor jsExecutor) {
        this.jsExecutor = jsExecutor;
    }

    private void init() {
        try {
            jsExecutor.executeScript("return window.test.size;");
        } catch (JavascriptException e) {
            jsExecutor.executeScript("window.test = new Map();");
        }
    }

    public void put(String item, String value) {
        init();
        jsExecutor.executeScript(String.format(
                "window.test.set('%s','%s');", item, value));
    }

    public String get(String key) {
        init();
        return (String) jsExecutor.executeScript(String.format(
                "return window.test.get('%s');", key));
    }


    public Long size() {
        init();
        return (Long) jsExecutor.executeScript("return window.test.size;");
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
