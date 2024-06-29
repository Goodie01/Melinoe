package nz.geek.goodwin.melinoe.framework.api.web.validation;

import nz.geek.goodwin.melinoe.framework.api.Validator;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

/**
 * @author Goodie
 */
public interface WebValidator extends Validator<WebDriver> {
    ValidationResult validate(final WebDriver webDriver);

    static WebValidator titleEquals(String title) {
        return titleImpl(StringUtils::equals, title);
    }

    static WebValidator titleContains(String title) {
        return titleImpl(StringUtils::contains, title);
    }

    private static WebValidator titleImpl(ToBooleanBiFunction<String, String> comparisonType, String expectedTitle) {
        return webDriver -> {
            String actualTitle = webDriver.getTitle();
            if (comparisonType.applyAsBoolean(actualTitle, expectedTitle)) {
                return ValidationResult.passed("Found expected title: " + expectedTitle);
            } else {
                return ValidationResult.failed("Title is not as expected",
                        String.format("Expected title: \"%s\"", expectedTitle),
                        String.format("Actual title: \"%s\"", actualTitle));
            }
        };
    }
}
