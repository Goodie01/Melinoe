package org.goodiemania.melinoe.framework.api.web;

import java.util.Arrays;
import java.util.List;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.web.validators.WebValidator;

public abstract class BasePage {
    private final Session session;
    private List<WebValidator> validators;

    public BasePage(final Session session, final WebValidator... validators) {
        this.session = session;
        this.validators = Arrays.asList(validators);
    }

    protected Session getSession() {
        return session;
    }

    public void checkPage() {
        session.web().checkPage(validators);
    }
}
