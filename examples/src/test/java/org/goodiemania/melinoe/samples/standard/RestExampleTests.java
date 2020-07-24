package org.goodiemania.melinoe.samples.standard;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.rest.validators.JsonPathValidator;
import org.goodiemania.melinoe.samples.GithubFlow;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RestExampleTests extends MelinoeTest {
    private GithubFlow githubFlow;


    @BeforeAll
    public static void init() {
        getClassSession().getLogger()
                .add()
                .withMessage("Before all");
        getClassSession().rest("https://jsonplaceholder.typicode.com/users")
                .execute()
                .validate(new JsonPathValidator("$.[0].id", "1"));
    }

    @AfterAll
    public static void tearDown() {
        getClassSession().getLogger()
                .add()
                .withMessage("After all");
        getClassSession().rest("https://jsonplaceholder.typicode.com/users")
                .execute()
                .validate(new JsonPathValidator("$.[0].id", "1"));
    }

    @Test
    @DisplayName("This is a basic rest test")
    public void runThisThing() {
        getSession().rest("https://jsonplaceholder.typicode.com/users")
                .execute()
                .validate(new JsonPathValidator("$.[0].id", "1"));
    }

    @Test
    @DisplayName("This is a basic rest test that executes in a sub session")
    public void subSessionTest() {
        final Session restSubSession = getSession().createSubSession("Rest sub session");
        restSubSession.rest("https://jsonplaceholder.typicode.com/users")
                .execute()
                .validate(new JsonPathValidator("$.[0].id", "1"));
    }
}
