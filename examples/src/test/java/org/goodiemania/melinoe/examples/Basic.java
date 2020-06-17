package org.goodiemania.melinoe.examples;

import org.goodiemania.melinoe.framework.MelinoeTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Basic extends MelinoeTest {
    @BeforeAll
    public static void init() {
        getClassContext();
    }

    @Test
    public void run() {
        getContext();
    }
}
