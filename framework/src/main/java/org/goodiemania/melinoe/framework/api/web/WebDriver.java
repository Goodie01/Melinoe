package org.goodiemania.melinoe.framework.api.web;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.goodiemania.melinoe.framework.api.web.validators.WebValidator;

public interface WebDriver {
    Navigate navigate();

    String getTitle();

    void checkPage(List<WebValidator> validators);

    void waitFor(Predicate<WebDriver> predicate);

    Optional<WebElement> findElement(By by);

    List<WebElement> findElements(By by);
}
