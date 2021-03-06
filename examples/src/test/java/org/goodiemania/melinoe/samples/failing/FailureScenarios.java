package org.goodiemania.melinoe.samples.failing;

import org.goodiemania.melinoe.framework.api.IgnoreFlowDecoration;
import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.samples.GithubFlow;
import org.goodiemania.melinoe.samples.GithubRepoPage;
import org.goodiemania.melinoe.samples.GithubRepoPullRequestPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FailureScenarios extends MelinoeTest {
    private GithubRepoPage githubRepoPage;
    private GithubRepoPullRequestPage githubRepoPullRequestPage;

    @IgnoreFlowDecoration
    private GithubFlow githubFlow;

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

    @Test
    @DisplayName("We can also pick up on random exceptions being thrown")
    public void exceptionThrownExample() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
        throw new MelinoeException("Tada!");
    }

    @Test
    @DisplayName("A flow/page/web element with the @IgnoreFlowDecoration annotation (on the class or field) won't be initialized by Melinoe")
    public void createFlowAsWeGo() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubFlow.navigateToPullRequestPage();
    }
}
