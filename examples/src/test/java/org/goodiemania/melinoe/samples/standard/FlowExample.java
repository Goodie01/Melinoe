package org.goodiemania.melinoe.samples.standard;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.samples.GithubFlow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FlowExample extends MelinoeTest {
    private GithubFlow githubFlow;

    @BeforeAll
    public static void init() {
        getClassSession();
    }

    @Test
    @DisplayName("This is an example using a flow")
    public void run() {
        getSession().web().navigate().to("https://github.com/Goodie01/Melinoe");
        githubFlow.navigateToPullRequestPage();
    }
}
