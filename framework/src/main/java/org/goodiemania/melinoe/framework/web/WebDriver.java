package org.goodiemania.melinoe.framework.web;

import java.util.List;
import org.goodiemania.melinoe.framework.web.validators.WebValidator;

public interface WebDriver {
    Navigate navigate();

    String getTitle();

    void checkPage(List<WebValidator> validators);
}
