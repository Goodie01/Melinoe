package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.web.BasePage;
import org.goodiemania.melinoe.framework.drivers.web.validators.TitleValidator;

public class GithubRepoPullRequestPage extends BasePage {
    public GithubRepoPullRequestPage(final Session session) {
        super(session, new TitleValidator("Pull requests · Goodie01/Melinoe · GitHub"));
    }
}
