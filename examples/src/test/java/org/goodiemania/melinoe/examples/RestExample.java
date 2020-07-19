package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.framework.api.rest.validators.JsonPathValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RestExample extends MelinoeTest {
    private GithubFlow githubFlow;

    @BeforeAll
    public static void init() {
        getClassSession();
    }

    @Test
    @DisplayName("This is a basic rest test")
    public void runThisThing() {
        getSession().rest("https://jsonplaceholder.typicode.com/users")
                .execute()
                .validate(new JsonPathValidator("$.[0].id", "1"));
    }
}
