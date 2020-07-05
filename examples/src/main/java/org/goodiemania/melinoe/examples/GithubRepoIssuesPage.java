package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.web.BasePage;
import org.goodiemania.melinoe.framework.drivers.web.validators.TitleValidator;

public class GithubRepoIssuesPage extends BasePage {
    public GithubRepoIssuesPage(final Session session) {
        super(session, new TitleValidator("Issues · Goodie01/Melinoe · GitHub"));
    }
}
