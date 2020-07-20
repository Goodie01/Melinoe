package org.goodiemania.melinoe.samples;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.web.validators.TitleValidator;
import org.goodiemania.melinoe.framework.drivers.web.BasePage;

public class GithubRepoPullRequestPage extends BasePage {
    public GithubRepoPullRequestPage(final Session session) {
        super(session, new TitleValidator("Pull requests · Goodie01/Melinoe · GitHub"));
    }
}
