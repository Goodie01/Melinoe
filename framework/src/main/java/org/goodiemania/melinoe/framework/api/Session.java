package org.goodiemania.melinoe.framework.api;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.goodiemania.melinoe.framework.api.hecate.HecateDriver;
import org.goodiemania.melinoe.framework.api.rest.RestRequest;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.validators.Validator;

public interface Session {
    RestRequest rest(final String uri);

    RestRequest rest(final URI uri);

    WebDriver web();

    HecateDriver hecate(final String uri, final int port);

    Logger getLogger();

    void decorate(Object object);

    void decorateClass(final Class<?> classType);

    Session createSubSession(final String name);

    void verify(final List<Validator> validators);

    default void verify(final Validator... validators) {
        verify(Arrays.asList(validators));
    }
}
