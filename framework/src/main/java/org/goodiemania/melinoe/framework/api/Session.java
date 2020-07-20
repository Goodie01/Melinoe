package org.goodiemania.melinoe.framework.api;

import java.net.URI;
import org.goodiemania.melinoe.framework.api.rest.RestRequest;
import org.goodiemania.melinoe.framework.api.web.BasePage;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public interface Session {
    RestRequest rest(final String uri);

    RestRequest rest(final URI uri);

    WebDriver web();

    Logger getLogger();

    void decorate(Object object);
}
