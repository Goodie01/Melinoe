package org.goodiemania.melinoe.framework.api;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;

public interface WebElement {
    void click();

    void submit();

    void sendKeys(CharSequence... keysToSend);

    void clear();

    String getTagName();

    String getAttribute(String name);

    boolean isSelected();

    boolean isEnabled();

    String getText();

    boolean isDisplayed();

    Point getLocation();

    //todo decide if we want these
    Dimension getSize();

    Rectangle getRect();
}
