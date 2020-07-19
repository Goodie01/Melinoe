package org.goodiemania.melinoe.framework.api.web;

import java.util.List;
import java.util.function.Predicate;
import org.goodiemania.melinoe.framework.api.web.validators.WebValidator;

public interface WebDriver {
    Navigate navigate();

    String getTitle();

    void checkPage(List<WebValidator> validators);

    void waitFor(Predicate<WebDriver> predicate);
}
