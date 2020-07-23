package org.goodiemania.melinoe.framework.drivers.web;

import org.goodiemania.melinoe.framework.api.web.Navigate;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public class NavigateImpl implements Navigate {
    private Logger logger;
    private RawWebDriver rawWebDriver;

    public NavigateImpl(final Logger logger, final RawWebDriver rawWebDriver) {
        this.logger = logger;
        this.rawWebDriver = rawWebDriver;
    }

    @Override
    public void back() {
        logger.add()
                .withMessage("Pressing back button on browser");
        rawWebDriver.getRemoteWebDriver().navigate().back();
    }

    @Override
    public void forward() {
        logger.add()
                .withMessage("Pressing forward button on browser");
        rawWebDriver.getRemoteWebDriver().navigate().forward();
    }

    @Override
    public void to(final String url) {
        logger.add()
                .withMessage("Going to: " + url);
        rawWebDriver.getRemoteWebDriver().navigate().to(url);
    }

    @Override
    public void refresh() {
        logger.add()
                .withMessage("Refreshing browser");
        rawWebDriver.getRemoteWebDriver().navigate().refresh();
    }
}
