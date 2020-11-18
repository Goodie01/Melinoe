package org.goodiemania.melinoe.samples.standard;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.samples.GithubRepoPage;
import org.goodiemania.melinoe.samples.GithubRepoPullRequestPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Basic examples")
public class Basic extends MelinoeTest {
    private static GithubRepoPage staticGithubRepoPage;
    private static GithubRepoPullRequestPage staticGithubRepoPullRequestPage;
    private GithubRepoPage githubRepoPage;
    private GithubRepoPullRequestPage githubRepoPullRequestPage;

    @BeforeAll
    public static void init() {
        getClassSession().getLogger()
                .add()
                .withMessage("Before all");
        getClassSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        staticGithubRepoPage.checkPage();
        staticGithubRepoPage.clickPullRequestLink();
        staticGithubRepoPullRequestPage.checkPage();
    }

    @AfterAll
    public static void tearDown() {
        getClassSession().getLogger()
                .add()
                .withMessage("After all");
        getClassSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        staticGithubRepoPage.checkPage();
        staticGithubRepoPage.clickPullRequestLink();
        staticGithubRepoPullRequestPage.checkPage();
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
    @DisplayName("Baseline test with a subcontext")
    public void subContextTest() {
        getSession().web().navigate().to("https://twitter.com");
        final Session subSession = getSession().createSubSession("Ohboi here we go");
        githubRepoPage = new GithubRepoPage(subSession);
        githubRepoPullRequestPage = new GithubRepoPullRequestPage(subSession);

        subSession.web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
    }

    @Test
    @DisplayName("Baseline test that interacts with a list of elements")
    public void runAgain() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPage.getFileTypes()
                .forEach(s -> getSession().getLogger()
                        .add()
                        .withMessage(s));
    }
}
