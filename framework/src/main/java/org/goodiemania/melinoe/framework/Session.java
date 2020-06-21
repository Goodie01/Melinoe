package org.goodiemania.melinoe.framework;

import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.web.WebDriver;

public interface Session {
    void rest();

    WebDriver web();

    ClassLogger getLogger();
}
