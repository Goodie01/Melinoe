package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Basic extends MelinoeTest {
    private GithubRepoPage githubRepoPage;
    private GithubRepoIssuesPage githubRepoIssuesPage;

    @BeforeAll
    public static void init() {
        getClassSession();
    }

    @AfterAll
    public static void tearDown() {
        getClassSession();
    }

    @Test
    @DisplayName("Run baseline test")
    public void run() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubRepoPage.checkPage();
        githubRepoPage.clickIssuesLink();
        githubRepoIssuesPage.checkPage();
    }
}
