package org.goodiemania.melinoe.samples.standard;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.internal.MelinoeExtension;
import org.goodiemania.melinoe.samples.GithubRepoPage;
import org.goodiemania.melinoe.samples.GithubRepoPullRequestPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MelinoeExtension.class)
public class Misc {
    @BeforeAll
    public static void initAll() {
        System.out.println("DISP: " + MelinoeExtension.DISPLAY_NAME);
        System.out.println("METH: " + MelinoeExtension.METHOD_NAME);
        System.out.println("CLSS: " + MelinoeExtension.CLASS_NAME);
        System.out.println("THRO: " + MelinoeExtension.THROWABLE);
    }

    @AfterAll
    public static void tearDownAll() {
        System.out.println("DISP: " + MelinoeExtension.DISPLAY_NAME);
        System.out.println("METH: " + MelinoeExtension.METHOD_NAME);
        System.out.println("CLSS: " + MelinoeExtension.CLASS_NAME);
        System.out.println("THRO: " + MelinoeExtension.THROWABLE);
    }
    @BeforeEach
    public void init() {
        System.out.println("DISP: " + MelinoeExtension.DISPLAY_NAME);
        System.out.println("METH: " + MelinoeExtension.METHOD_NAME);
        System.out.println("CLSS: " + MelinoeExtension.CLASS_NAME);
        System.out.println("THRO: " + MelinoeExtension.THROWABLE);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("DISP: " + MelinoeExtension.DISPLAY_NAME);
        System.out.println("METH: " + MelinoeExtension.METHOD_NAME);
        System.out.println("CLSS: " + MelinoeExtension.CLASS_NAME);
        System.out.println("THRO: " + MelinoeExtension.THROWABLE);
    }

    @Test
    @DisplayName("Run")
    public void run() {
        System.out.println("DISP: " + MelinoeExtension.DISPLAY_NAME);
        System.out.println("METH: " + MelinoeExtension.METHOD_NAME);
        System.out.println("CLSS: " + MelinoeExtension.CLASS_NAME);
        System.out.println("THRO: " + MelinoeExtension.THROWABLE);
    }

    @Test
    @DisplayName("Run 2")
    public void run2() {
        System.out.println("DISP: " + MelinoeExtension.DISPLAY_NAME);
        System.out.println("METH: " + MelinoeExtension.METHOD_NAME);
        System.out.println("CLSS: " + MelinoeExtension.CLASS_NAME);
        System.out.println("THRO: " + MelinoeExtension.THROWABLE);
    }
}
