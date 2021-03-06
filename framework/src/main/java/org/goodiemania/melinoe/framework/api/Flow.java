package org.goodiemania.melinoe.framework.api;

public abstract class Flow {
    private final Session session;

    public Flow(final Session session) {
        this.session = session;

        session.decorate(this);
    }

    protected Session getSession() {
        return session;
    }
}
