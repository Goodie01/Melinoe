package org.goodiemania.melinoe.framework.api.web.validators;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.ValidationResult;
import org.goodiemania.melinoe.framework.api.web.By;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.api.web.WebElement;

public class WebElementContains implements WebValidator {
    private final By elementFinder;
    private final String searchText;
    private final WebElement webElement;

    public WebElementContains(final By elementFinder, final String searchText) {
        this.elementFinder = elementFinder;
        this.searchText = searchText;
        this.webElement = null;
    }

    public WebElementContains(final WebElement webElement, final String searchText) {
        this.elementFinder = null;
        this.searchText = searchText;
        this.webElement = webElement;
    }

    @Override
    public ValidationResult validate(final WebDriver webDriver) {
        if(elementFinder != null) {
            final String possibleElementText = webDriver.findElement(elementFinder)
                    .getText();

            if (possibleElementText.isEmpty()) {
                return ValidationResult.failed("Could not find element",
                        "element search by: " + elementFinder.getType() + ":" + elementFinder.getText());
            } else if (StringUtils.contains(possibleElementText, searchText)) {
                return ValidationResult.passed("Found expected text: " + searchText,
                        "actual text: " + possibleElementText,
                        "element search by: " + elementFinder.getType() + ":" + elementFinder.getText());
            } else {
                return ValidationResult.failed("Could not find expected text: " + searchText,
                        "actual text: " + possibleElementText,
                        "element search by: " + elementFinder.getType() + " " + elementFinder.getText());
            }
        } else if (webElement != null) {
            final String possibleText = webElement.getText();

            if (possibleText.isEmpty()) {
                return ValidationResult.failed("Could not find text");
            } else if (StringUtils.equals(possibleText, searchText)) {
                return ValidationResult.passed("Found expected text: " + searchText);
            } else {
                return ValidationResult.failed("Could not find expected text: " + searchText,
                        "actual text: " + possibleText);
            }
        } else {
            return ValidationResult.failed("Something went very wrong");
        }
    }
}
