package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.web.Navigate;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Goodie
 */
public class NavigateImpl implements Navigate {
    private final RemoteWebDriver remoteWebDriver;
    private final Logger logger;

    public NavigateImpl(RemoteWebDriver remoteWebDriver, Logger logger) {
        this.remoteWebDriver = remoteWebDriver;
        this.logger = logger;
    }

    @Override
    public void back() {
        logger.add().withMessage("Pressing browser back button");
        remoteWebDriver.navigate().back();
    }

    @Override
    public void forward() {
        logger.add().withMessage("Pressing browser forward button");
        remoteWebDriver.navigate().forward();
    }

    @Override
    public void to(String url) {
        logger.add().withMessage("Navigating to URL: " + url);
        remoteWebDriver.navigate().to(url);
    }

    @Override
    public void refresh() {
        logger.add().withMessage("Pressing browser refresh button");
        remoteWebDriver.navigate().refresh();
    }

    @Override
    public String getUrl() {
        return remoteWebDriver.getCurrentUrl();
    }
}
