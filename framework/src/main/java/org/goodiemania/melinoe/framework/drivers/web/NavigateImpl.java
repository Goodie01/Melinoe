package org.goodiemania.melinoe.framework.drivers.web;

import org.goodiemania.melinoe.framework.api.web.Navigate;
import org.goodiemania.melinoe.framework.session.InternalSession;

public class NavigateImpl implements Navigate {
    private InternalSession internalSession;
    private RawWebDriver rawWebDriver;

    public NavigateImpl(final InternalSession internalSession, final RawWebDriver rawWebDriver) {
        this.internalSession = internalSession;
        this.rawWebDriver = rawWebDriver;
    }

    @Override
    public void back() {
        internalSession.getSession().getLogger().add()
                .withMessage("Pressing back button on browser");
        rawWebDriver.getRemoteWebDriver().navigate().back();
    }

    @Override
    public void forward() {
        internalSession.getSession().getLogger().add()
                .withMessage("Pressing forward button on browser");
        rawWebDriver.getRemoteWebDriver().navigate().forward();
    }

    @Override
    public void to(final String url) {
        internalSession.getSession().getLogger().add()
                .withMessage("Going to: " + url);
        rawWebDriver.getRemoteWebDriver().navigate().to(url);
    }

    @Override
    public void refresh() {
        internalSession.getSession().getLogger().add()
                .withMessage("Refreshing browser");
        rawWebDriver.getRemoteWebDriver().navigate().refresh();
    }
}
