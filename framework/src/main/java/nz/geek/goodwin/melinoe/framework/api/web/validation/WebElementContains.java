package nz.geek.goodwin.melinoe.framework.api.web.validation;

import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import org.apache.commons.lang3.StringUtils;

public class WebElementContains implements WebValidator {
    private final String searchText;
    private final WebElement webElement;

    public WebElementContains(final WebElement webElement, final String searchText) {
        this.searchText = searchText;
        this.webElement = webElement;
    }

    @Override
    public ValidationResult validate(final WebDriver webDriver) {
        final String possibleText = webElement.getText();

        if (possibleText.isEmpty()) {
            return ValidationResult.failed("Could not find text");
        } else if (StringUtils.contains(possibleText, searchText)) {
            return ValidationResult.passed("Found expected text: " + searchText);
        } else {
            return ValidationResult.failed("Could not find expected text: " + searchText,
                    "actual text: " + possibleText);
        }
    }
}
