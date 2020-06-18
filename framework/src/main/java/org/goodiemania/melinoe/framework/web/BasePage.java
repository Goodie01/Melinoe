package org.goodiemania.melinoe.framework.web;

import org.goodiemania.melinoe.framework.Session;

public class BasePage {
    private final Session session;

    public BasePage(final Session session) {
        this.session = session;
    }

    public void checkPage() {
        session.web().checkPage();
    }
}
