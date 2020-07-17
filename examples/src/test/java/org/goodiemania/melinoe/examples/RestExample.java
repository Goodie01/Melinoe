package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RestExample extends MelinoeTest {
    private GithubFlow githubFlow;

    @BeforeAll
    public static void init() {
        getClassSession();
    }

    @Test
    public void runThisThing() {
        getSession().rest("https://jsonplaceholder.typicode.com/users")
                .execute();
    }
}
