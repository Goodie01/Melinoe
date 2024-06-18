package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.web.By;

public class ConvertBy {
    private ConvertBy() {
    }

    public static org.openqa.selenium.By build(final By by) {
        switch (by.getType()) {
            case ID:
                return org.openqa.selenium.By.id(by.getText());
            case CLASS_NAME:
                return org.openqa.selenium.By.className(by.getText());
            case CSS_SELECTOR:
                return org.openqa.selenium.By.cssSelector(by.getText());
            case NAME:
                return org.openqa.selenium.By.name(by.getText());
            case LINK_TEXT:
                return org.openqa.selenium.By.linkText(by.getText());
            case PARTIAL_LINK_TEXT:
                return org.openqa.selenium.By.partialLinkText(by.getText());
            case TAG_NAME:
                return org.openqa.selenium.By.tagName(by.getText());
            case X_PATH:
                return org.openqa.selenium.By.xpath(by.getText());
            default:
                throw new MelinoeException("Unknown by type");
        }
    }
}
