package org.goodiemania.melinoe.samples.standard;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.internal.MelinoeExtension;
import org.goodiemania.melinoe.samples.mozilla.MozillaHomepage;
import org.goodiemania.melinoe.samples.mozilla.MozillaManifestopage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MelinoeExtension.class)
public class MozillaTest {
    private Session session;
    private MozillaHomepage mozillaHomepage;

    private MozillaManifestopage mozillaManifestopage;
    @Test
    @DisplayName("Run")
    public void run() {
        session = Session.create();
        mozillaHomepage = new MozillaHomepage(session);
        mozillaManifestopage = new MozillaManifestopage(session);

        session.web().navigate().to("https://www.mozilla.org/en-US/");
        mozillaHomepage.checkPage();
        mozillaHomepage.clickManifestoLink();

        mozillaManifestopage.checkPage();
        mozillaManifestopage.validateSubtitle();
    }
}
