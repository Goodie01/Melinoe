package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.MelinoeTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Basic extends MelinoeTest {
    private GithubRepositoryPage githubRepositoryPage;

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
        getSession().web().get("https://github.com/Goodie01/Melinoe");
        githubRepositoryPage.checkPage();
    }
}
