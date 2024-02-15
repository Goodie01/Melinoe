package nz.geek.goodwin.melinoe.framework.api.web.validation;

import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

import java.util.function.Supplier;

public class WebElementText implements WebValidator {
    private final String searchText;
    private final WebElement webElement;
    private final ToBooleanBiFunction<String, String> comparisonType;

    private WebElementText(final WebElement webElement, final String searchText, ToBooleanBiFunction<String, String> comparisonType) {
        this.searchText = searchText;
        this.webElement = webElement;
        this.comparisonType = comparisonType;
    }

    public static WebElementText contains(final WebElement webElement, final String searchText) {
        return new WebElementText(webElement, searchText, StringUtils::contains);
    }

    public static WebElementText equals(final WebElement webElement, final String searchText) {
        return new WebElementText(webElement, searchText, StringUtils::equals);
    }

    @Override
    public ValidationResult validate(final WebDriver webDriver) {
        final String possibleText = webElement.getText();

        if (possibleText.isEmpty()) {
            return ValidationResult.failed("Could not find text");
        } else if (comparisonType.applyAsBoolean(possibleText, searchText)) {
            return ValidationResult.passed("Found expected text: " + searchText);
        } else {
            return ValidationResult.failed("Could not find expected text: " + searchText,
                    "actual text: " + possibleText);
        }
    }
}
