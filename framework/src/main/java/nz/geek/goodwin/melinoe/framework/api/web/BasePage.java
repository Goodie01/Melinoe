package nz.geek.goodwin.melinoe.framework.api.web;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;

import java.util.Arrays;
import java.util.List;

/**
 * @author Goodie
 */
public abstract class BasePage {
    private final Session session;
    private List<WebValidator> validators;

    public BasePage(final Session session, final WebValidator... validators) {
        this.session = session;
        this.validators = Arrays.asList(validators);

        session.web().decorate(this);
    }

    protected Session getSession() {
        return session;
    }

    public void checkPage() {
        session.web().verify(validators);
    }
}
