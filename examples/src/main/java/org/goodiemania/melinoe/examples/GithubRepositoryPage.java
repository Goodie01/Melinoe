package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.Session;
import org.goodiemania.melinoe.framework.web.BasePage;
import org.goodiemania.melinoe.framework.web.validators.TitleValidator;
import org.openqa.selenium.support.FindBy;

public class GithubRepositoryPage extends BasePage {
    @FindBy

    public GithubRepositoryPage(final Session session) {
        super(session, new TitleValidator("GitHub - Goodie01/Melinoe: Melinoe is named for the greek mythological figure who is known as a \"bringer of nightmares and madness\"; which seems apt for a library designed to enable automated testing"));
    }
}
