package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.MelinoeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Basic extends MelinoeTest {
    private GithubRepositoryPage githubRepositoryPage;

    @BeforeAll
    public static void init() {
        getClassSession();
    }

    @Test
    public void run() {
        getSession().web().get("https://github.com/Goodie01/Melinoe");
        githubRepositoryPage.checkPage();
    }
}