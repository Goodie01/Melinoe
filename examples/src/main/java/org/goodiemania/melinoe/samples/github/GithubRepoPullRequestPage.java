package org.goodiemania.melinoe.samples.github;

import nz.geek.goodwin.melinoe.framework.api.web.BasePage;
import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.validation.TitleValidator;

public class GithubRepoPullRequestPage extends BasePage {
    public GithubRepoPullRequestPage(final Session session) {
        super(session, TitleValidator.equals("Pull requests · Goodie01/Melinoe · GitHub"));
    }
}
