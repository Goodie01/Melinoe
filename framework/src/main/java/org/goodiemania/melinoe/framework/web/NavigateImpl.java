package org.goodiemania.melinoe.framework.web;
import org.openqa.selenium.WebDriver;

public class NavigateImpl implements Navigate{
    private final org.openqa.selenium.WebDriver webDriver;

    public NavigateImpl(final WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public void back() {
        webDriver.navigate().back();
    }

    @Override
    public void forward() {
        webDriver.navigate().forward();
    }

    @Override
    public void to(final String url) {
        webDriver.navigate().to(url);
    }

    @Override
    public void refresh() {
        webDriver.navigate().refresh();
    }
}
