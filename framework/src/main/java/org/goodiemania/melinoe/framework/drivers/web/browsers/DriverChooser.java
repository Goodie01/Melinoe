package org.goodiemania.melinoe.framework.drivers.web.browsers;

import java.util.function.Function;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverChooser {
    public static Function<String, RemoteWebDriver> chooseDriver(final String driverName) {
        switch (driverName) {
            case "FIREFOX":
                return Firefox::get;
            case "FIREFOX_HEADLESS":
                return FirefoxHeadless::get;
            default:
                throw new MelinoeException(
                        String.format("Invalid browser choice selected', '%s' was given", driverName)
                );
        }
    }
}
