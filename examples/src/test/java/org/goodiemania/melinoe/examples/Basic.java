package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Basic examples")
public class Basic extends MelinoeTest {
    private GithubRepoPage githubRepoPage;
    private GithubRepoPullRequestPage githubRepoPullRequestPage;

    @BeforeAll
    public static void init() {
        getClassSession().getLogger()
                .add()
                .withMessage("Before all");
    }

    @AfterAll
    public static void tearDown() {
        getClassSession().getLogger()
                .add()
                .withMessage("After all");
    }

    @Test
    @DisplayName("Baseline test")
    public void run() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
    }

    @Test
    @DisplayName("Baseline test that will fail")
    public void thisWillFail() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPullRequestPage.checkPage();
        githubRepoPage.clickPullRequestLink();
    }

    @Test
    @DisplayName("Baseline test that will fail, you must check a page before interacting with it")
    public void anotherFailure() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
    }
}
