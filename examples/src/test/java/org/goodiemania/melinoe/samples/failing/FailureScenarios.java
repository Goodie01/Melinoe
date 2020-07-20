package org.goodiemania.melinoe.samples.failing;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.samples.GithubRepoPage;
import org.goodiemania.melinoe.samples.GithubRepoPullRequestPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FailureScenarios extends MelinoeTest {
    private GithubRepoPage githubRepoPage;
    private GithubRepoPullRequestPage githubRepoPullRequestPage;

    @Test
    @DisplayName("These actions are simply out of order")
    public void thisWillFail() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPullRequestPage.checkPage();
        githubRepoPage.clickPullRequestLink();
    }

    @Test
    @DisplayName("You must check a page before interacting with it")
    public void anotherFailure() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
    }
}
