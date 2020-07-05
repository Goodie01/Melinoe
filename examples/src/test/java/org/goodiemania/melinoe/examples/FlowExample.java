package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FlowExample extends MelinoeTest {
    private GithubFlow githubFlow;

    @BeforeAll
    public static void init() {
        getClassSession();
    }

    @Test
    public void run() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubFlow.navigateToPullRequestPage();
    }
}
