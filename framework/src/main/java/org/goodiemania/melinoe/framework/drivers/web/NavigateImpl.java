package org.goodiemania.melinoe.framework.drivers.web;

import org.goodiemania.melinoe.framework.api.web.Navigate;
import org.goodiemania.melinoe.framework.session.InternalSession;

public class NavigateImpl implements Navigate {
    private InternalSession internalSession;

    public NavigateImpl(final InternalSession internalSession) {
        this.internalSession = internalSession;
    }

    @Override
    public void back() {
        internalSession.getLogger().add().withMessage("Pressing back button on browser");
        internalSession.getRawWebDriver().getRemoteWebDriver().navigate().back();
    }

    @Override
    public void forward() {
        internalSession.getLogger().add().withMessage("Pressing forward button on browser");
        internalSession.getRawWebDriver().getRemoteWebDriver().navigate().forward();
    }

    @Override
    public void to(final String url) {
        internalSession.getLogger().add().withMessage("Going to: " + url);
        internalSession.getRawWebDriver().getRemoteWebDriver().navigate().to(url);
    }

    @Override
    public void refresh() {
        internalSession.getLogger().add().withMessage("Refreshing browser");
        internalSession.getRawWebDriver().getRemoteWebDriver().navigate().refresh();
    }
}
