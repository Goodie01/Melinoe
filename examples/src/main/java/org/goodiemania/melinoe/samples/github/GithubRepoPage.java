package org.goodiemania.melinoe.samples.github;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.BasePage;
import nz.geek.goodwin.melinoe.framework.api.web.FindElement;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;

import java.util.List;
import java.util.stream.Collectors;

public class GithubRepoPage extends BasePage {
    @FindElement(linkText = "Pull requests")
    private WebElement pullRequestLink;

    @FindElement(css = "div.Box-row > div:nth-child(2) > span:nth-child(1)")
    private List<WebElement> fileList;

    public GithubRepoPage(final Session session) {
        super(session,
                WebValidator.titleEquals("GitHub - Goodie01/Melinoe: Melinoe is named for the greek mythological figure who is known as a \"bringer of nightmares and madness\", this seems apt for a automated testing framework"));
        //
    }

    public void clickPullRequestLink() {
        pullRequestLink.click();
        getSession().web().waitFor(webDriver -> webDriver.getTitle().equals("Pull requests · Goodie01/Melinoe · GitHub"));
    }

    public List<String> getFileTypes() {
        return fileList.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
