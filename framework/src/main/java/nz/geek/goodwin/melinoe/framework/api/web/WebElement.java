package nz.geek.goodwin.melinoe.framework.api.web;

import java.util.List;

/**
 * @author Goodie
 */
public interface WebElement {
    void hover();

    void click();

    void submit();

    void sendKeys(String stringToEnter);

    void clear();

    String getTagName();

    String getAttribute(String name);

    boolean isSelected();

    boolean isEnabled();

    String getText();

    List<WebElement> findElements(By by);

    WebElement findElement(By by);

    boolean isDisplayed();

    String getCssValue(String propertyName);
}