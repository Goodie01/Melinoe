package nz.geek.goodwin.melinoe.framework.api.web;

import nz.geek.goodwin.melinoe.framework.api.Session;

/**
 * @author Goodie
 */
public abstract class Flow {
    private final Session session;

    public Flow(final Session session) {
        this.session = session;

        session.web().decorate(this);
    }

    protected Session getSession() {
        return session;
    }
}

