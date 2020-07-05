package org.goodiemania.melinoe.framework.api;

import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.drivers.web.WebDriver;

public interface Session {
    void rest();

    WebDriver web();

    Logger getLogger();
}
