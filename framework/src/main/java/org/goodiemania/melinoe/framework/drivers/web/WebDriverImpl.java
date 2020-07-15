package org.goodiemania.melinoe.framework.drivers.web;

import java.util.List;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.api.web.validators.WebValidator;
import org.goodiemania.melinoe.framework.session.InternalSession;

public class WebDriverImpl implements ClosableDriver, WebDriver {
    private final RawWebDriver rawWebDriver;

    private Navigate navigate;

    public WebDriverImpl(final InternalSession internalSession, final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
        this.navigate = new NavigateImpl(internalSession, rawWebDriver);
    }

    @Override
    public void close() {
        this.rawWebDriver.close();
    }

    @Override
    public Navigate navigate() {
        return navigate;
    }

    @Override
    public void checkPage(final List<WebValidator> validators) {
        rawWebDriver.checkPage(validators);
    }

    @Override
    public String getTitle() {
        return rawWebDriver.getRemoteWebDriver().getTitle();
    }
}
