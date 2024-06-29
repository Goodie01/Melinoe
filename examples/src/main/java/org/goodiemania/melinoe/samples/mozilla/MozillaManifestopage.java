package org.goodiemania.melinoe.samples.mozilla;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.BasePage;
import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.FindElement;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebElementText;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;

/**
 * @author Goodie
 */
public class MozillaManifestopage extends BasePage {
    @FindElement(css = ".addendum-subtitle")
    private WebElement manifestoSubTitle;

    public MozillaManifestopage(Session session) {
        super(session, WebValidator.titleEquals("The Mozilla Manifesto"),
                WebElementText.equals(By.cssSelector(".addendum-subtitle"), "Pledge for a Healthy Internet"));

    }

    public void validateSubtitle() {
        getSession().web().verify(WebElementText.equals(manifestoSubTitle, "Pledge for a Healthy Internet"));
    }
}
