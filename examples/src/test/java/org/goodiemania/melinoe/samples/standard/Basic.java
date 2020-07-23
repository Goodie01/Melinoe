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
