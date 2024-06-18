package org.goodiemania.melinoe.samples.standard;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.internal.MelinoeExtension;
import org.goodiemania.melinoe.samples.github.GithubRepoPage;
import org.goodiemania.melinoe.samples.github.GithubRepoPullRequestPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MelinoeExtension.class)
public class GitHubTest {
    private static GithubRepoPage staticGithubRepoPage;
    private static GithubRepoPullRequestPage staticGithubRepoPullRequestPage;
    private static Session staticSession;
    private Session session;
    private GithubRepoPage githubRepoPage;
    private GithubRepoPullRequestPage githubRepoPullRequestPage;

    @BeforeAll
    public static void initAll() {
        staticSession = Session.create();
        staticSession.log().add()
                .withMessage("Before all");
        staticSession.web().navigate().to("https://github.com/Goodie01/Melinoe");
        staticGithubRepoPage = new GithubRepoPage(staticSession);
        staticGithubRepoPullRequestPage = new GithubRepoPullRequestPage(staticSession);

        staticGithubRepoPage.checkPage();
        staticGithubRepoPage.clickPullRequestLink();
        staticGithubRepoPullRequestPage.checkPage();
    }

    @AfterAll
    public static void tearDownAll() {
        staticSession = Session.create();
        staticSession.log()
                .add()
                .withMessage("After all");
        staticSession.web().navigate().to("https://github.com/Goodie01/Melinoe");
        staticGithubRepoPage = new GithubRepoPage(staticSession);
        staticGithubRepoPullRequestPage = new GithubRepoPullRequestPage(staticSession);

        staticGithubRepoPage.checkPage();
        staticGithubRepoPage.clickPullRequestLink();
        staticGithubRepoPullRequestPage.checkPage();
        Session.closeAll();
    }

    @BeforeEach
    public void init() {
        session = Session.create();
        session.log().add().withMessage("Before each");
        session.web().navigate().to("https://github.com/Goodie01/Melinoe");
        staticGithubRepoPage = new GithubRepoPage(session);
        staticGithubRepoPullRequestPage = new GithubRepoPullRequestPage(session);

        staticGithubRepoPage.checkPage();
        staticGithubRepoPage.clickPullRequestLink();
        staticGithubRepoPullRequestPage.checkPage();
    }

    @AfterEach
    public void tearDown() {
        session = Session.create();
        session.log().add().withMessage("After each");
        session.web().navigate().to("https://github.com/Goodie01/Melinoe");
        staticGithubRepoPage = new GithubRepoPage(session);
        staticGithubRepoPullRequestPage = new GithubRepoPullRequestPage(session);

        staticGithubRepoPage.checkPage();
        staticGithubRepoPage.clickPullRequestLink();
        staticGithubRepoPullRequestPage.checkPage();
    }

    @Test
    @DisplayName("Baseline test")
    public void run() {
        session = Session.create();
        githubRepoPage = new GithubRepoPage(session);
        githubRepoPullRequestPage = new GithubRepoPullRequestPage(session);

        session.web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPage.clickPullRequestLink();
        githubRepoPullRequestPage.checkPage();
    }

    @Test
    @DisplayName("Baseline test with a subcontext")
    public void subContextTest() {
        session = Session.create();
        session.web().navigate().to("https://twitter.com");
        final Session subSession = session.createChildSession("Ohboi here we go");
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
        session = Session.create();
        session.web().navigate().to("https://github.com/Goodie01/Melinoe");

        githubRepoPage = new GithubRepoPage(session);
        githubRepoPullRequestPage = new GithubRepoPullRequestPage(session);

        githubRepoPage.checkPage();
        githubRepoPage.getFileTypes()
                .forEach(s -> session.log()
                        .add()
                        .withMessage(s));
    }
}
