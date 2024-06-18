package org.goodiemania.melinoe.samples.standard;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.rest.validation.RestValidator;
import nz.geek.goodwin.melinoe.framework.internal.MelinoeExtension;
import org.goodiemania.melinoe.samples.mozilla.MozillaHomepage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(MelinoeExtension.class)
public class FailingTests {
    private Session session;
    private MozillaHomepage mozillaHomepage;
    @Test
    @DisplayName("Run, checking for a page and being on the wrong page")
    public void runWithBadTitle() {
        session = Session.create();
        mozillaHomepage = new MozillaHomepage(session);

        session.web().navigate().to("https://www.mozilla.org/en-US/");
        mozillaHomepage.checkPage();
        mozillaHomepage.clickManifestoLink();

        mozillaHomepage.checkPage();
    }
    @Test
    @DisplayName("Run, dont't validate page")
    public void runWithOutChecking() {
        session = Session.create();
        mozillaHomepage = new MozillaHomepage(session);

        session.web().navigate().to("https://www.mozilla.org/en-US/");
        mozillaHomepage.clickManifestoLink();
    }

    @Test
    @DisplayName("Run with exception")
    public void runWithException() {
        session = Session.create();
        throw new RuntimeException("This test should fail");
    }

    @Test
    @DisplayName("Baseline test")
    public void run() {
        session = Session.create();
        session.rest().get("https://1.1.1.1/dns-query?name=cloudflare.com")
                .header("accept", "application/dns-json")
                .result()
                .verify(List.of(RestValidator.statusCodeEquals(201)));
    }
}
