package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.web.Navigate;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Goodie
 */
public class NavigateImpl implements Navigate {
    private final RemoteWebDriver remoteWebDriver;

    public NavigateImpl(RemoteWebDriver remoteWebDriver) {
        this.remoteWebDriver = remoteWebDriver;
    }

    @Override
    public void back() {
        remoteWebDriver.navigate().back();
    }

    @Override
    public void forward() {
        remoteWebDriver.navigate().forward();
    }

    @Override
    public void to(String url) {
        remoteWebDriver.navigate().to(url);
    }

    @Override
    public void refresh() {
        remoteWebDriver.navigate().refresh();
    }
}
