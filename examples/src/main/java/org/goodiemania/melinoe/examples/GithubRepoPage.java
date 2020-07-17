package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.web.FindElement;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.api.web.validators.TitleValidator;
import org.goodiemania.melinoe.framework.drivers.web.BasePage;

public class GithubRepoPage extends BasePage {
    @FindElement(linkText = "Pull requests")
    private WebElement pullRequestLink;

    public GithubRepoPage(final Session session) {
        super(session, new TitleValidator("GitHub - Goodie01/Melinoe: Melinoe is named for the greek mythological figure who is known as a \"bringer of nightmares and madness\"; which seems apt for a library designed to enable automated testing"));
    }

    public void clickPullRequestLink() {
        //TODO handle clicking this
        //  This is a AJAX link and will require a wait for here
        pullRequestLink.click();
        getSession().web().waitFor(webDriver -> webDriver.getTitle().equals("Pull requests · Goodie01/Melinoe · GitHub"));
    }
}
