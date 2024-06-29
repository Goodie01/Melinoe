package org.goodiemania.melinoe.samples.mozilla;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.BasePage;
import nz.geek.goodwin.melinoe.framework.api.web.FindElement;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;

/**
 * @author Goodie
 */
public class MozillaHomepage extends BasePage {
    @FindElement(linkText = "Mozilla Manifesto")
    private WebElement manifestoLink;

    public MozillaHomepage(Session session) {
        super(session, WebValidator.titleEquals("Internet for people, not profit — Mozilla (US)"));
    }

    public void clickManifestoLink() {
        manifestoLink.click();
    }
}
