package org.goodiemania.melinoe.samples.github;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.BasePage;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;

public class GithubRepoPullRequestPage extends BasePage {
    public GithubRepoPullRequestPage(final Session session) {
        super(session, WebValidator.titleEquals("Pull requests · Goodie01/Melinoe · GitHub"));
    }
}
