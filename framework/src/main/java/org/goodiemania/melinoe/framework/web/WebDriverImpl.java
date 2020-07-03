package org.goodiemania.melinoe.framework.web;

import java.util.List;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.web.validators.WebValidator;

public class WebDriverImpl implements ClosableDriver, WebDriver {
    private final RawWebDriver rawWebDriver;

    private NavigateImpl navigate;

    public WebDriverImpl(final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
        this.navigate = new NavigateImpl(rawWebDriver);
    }

    @Override
    public void close() {
        //TODO we should handle this betterer
        this.rawWebDriver.remoteWebDriver().close();
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
        return rawWebDriver.remoteWebDriver().getTitle();
    }
}
