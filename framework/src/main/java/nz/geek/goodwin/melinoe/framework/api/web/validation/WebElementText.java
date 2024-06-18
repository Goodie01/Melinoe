package nz.geek.goodwin.melinoe.framework.api.web.validation;

import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

import java.util.function.Function;
import java.util.function.Supplier;

public class WebElementText implements WebValidator {
    private final String searchText;
    private final Function<WebDriver, WebElement> webDriverFunction;
    private final ToBooleanBiFunction<String, String> comparisonType;

    private WebElementText(final Function<WebDriver, WebElement> webDriverFunction, final String searchText, ToBooleanBiFunction<String, String> comparisonType) {
        this.searchText = searchText;
        this.webDriverFunction = webDriverFunction;
        this.comparisonType = comparisonType;
    }

    public static WebElementText contains(final By by, final String searchText) {
        return new WebElementText(webDriver -> webDriver.findElement(by), searchText, StringUtils::contains);
    }

    public static WebElementText equals(final By by, final String searchText) {
        return new WebElementText(webDriver -> webDriver.findElement(by), searchText, StringUtils::equals);
    }

    public static WebElementText contains(final WebElement webElement, final String searchText) {
        return new WebElementText(webDriver -> webElement, searchText, StringUtils::contains);
    }

    public static WebElementText equals(final WebElement webElement, final String searchText) {
        return new WebElementText(webDriver -> webElement, searchText, StringUtils::equals);
    }

    @Override
    public ValidationResult validate(final WebDriver webDriver) {
        final String possibleText = webDriverFunction.apply(webDriver).getText();

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
