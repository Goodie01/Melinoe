package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.FindElement;
import org.goodiemania.melinoe.framework.api.WebElement;
import org.goodiemania.melinoe.framework.drivers.web.BasePage;
import org.goodiemania.melinoe.framework.drivers.web.validators.TitleValidator;

public class GithubRepoPage extends BasePage {
    @FindElement(linkText = "Issues")
    private WebElement issuesLink;

    public GithubRepoPage(final Session session) {
        super(session, new TitleValidator("GitHub - Goodie01/Melinoe: Melinoe is named for the greek mythological figure who is known as a \"bringer of nightmares and madness\"; which seems apt for a library designed to enable automated testing"));
    }

    public void clickIssuesLink() {
        issuesLink.click();
    }
}
