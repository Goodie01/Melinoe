package org.goodiemania.melinoe.framework.api;

import java.net.URI;
import org.goodiemania.melinoe.framework.drivers.RestRequest;
import org.goodiemania.melinoe.framework.drivers.web.WebDriver;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public interface Session {
    RestRequest rest(final String uri);
    RestRequest rest(final URI uri);

    WebDriver web();

    Logger getLogger();
}
