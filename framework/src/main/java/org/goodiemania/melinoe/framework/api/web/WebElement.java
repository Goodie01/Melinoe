package org.goodiemania.melinoe.framework.api.web;

import java.util.List;
import java.util.Optional;
import org.goodiemania.melinoe.framework.api.misc.Dimension;
import org.goodiemania.melinoe.framework.api.misc.Point;
import org.goodiemania.melinoe.framework.api.misc.Rectangle;

public interface WebElement {
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

    Optional<WebElement> findElement(By by);

    boolean isDisplayed();

    Point getLocation();

    Dimension getSize();

    Rectangle getRect();

    String getCssValue(String propertyName);
}
