package org.goodiemania.melinoe.framework.web;

public class NavigateImpl implements Navigate {
    private RawWebDriver rawWebDriver;

    public NavigateImpl(final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
    }

    @Override
    public void back() {
        rawWebDriver.remoteWebDriver().navigate().back();
    }

    @Override
    public void forward() {
        rawWebDriver.remoteWebDriver().navigate().forward();
    }

    @Override
    public void to(final String url) {
        rawWebDriver.remoteWebDriver().navigate().to(url);
    }

    @Override
    public void refresh() {
        rawWebDriver.remoteWebDriver().navigate().refresh();
    }
}
