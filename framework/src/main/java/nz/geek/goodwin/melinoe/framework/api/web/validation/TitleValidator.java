package nz.geek.goodwin.melinoe.framework.api.web.validation;

import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

/**
 * Created on 28/06/2019.
 */
public class TitleValidator implements WebValidator {
    private final String expectedTitle;
    private final ToBooleanBiFunction<String, String> comparisonType;

    private TitleValidator(final String expectedTitle, ToBooleanBiFunction<String, String> comparisonType) {
        this.expectedTitle = expectedTitle;
        this.comparisonType = comparisonType;
    }

    public static TitleValidator contains(final String searchText) {
        return new TitleValidator(searchText, StringUtils::contains);
    }

    public static TitleValidator equals(final String searchText) {
        return new TitleValidator(searchText, StringUtils::equals);
    }

    @Override
    public ValidationResult validate(final WebDriver webDriver) {
        String actualTitle = webDriver.getTitle();
        if (comparisonType.applyAsBoolean(actualTitle, expectedTitle)) {
            return ValidationResult.passed("Found expected title: " + expectedTitle);
        } else {
            return ValidationResult.failed("Title is not as expected",
                    String.format("Expected title: \"%s\"", expectedTitle),
                    String.format("Actual title: \"%s\"", actualTitle));
        }
    }
}
