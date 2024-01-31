package nz.geek.goodwin.melinoe.framework.api.web.validation;

import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import org.apache.commons.lang3.StringUtils;

/**
 * Created on 28/06/2019.
 */
public class TitleValidator implements WebValidator {
    private final String expectedTitle;

    public TitleValidator(final String expectedTitle) {
        this.expectedTitle = expectedTitle;
    }

    @Override
    public ValidationResult validate(final WebDriver webDriver) {
        String actualTitle = webDriver.getTitle();
        if (StringUtils.equals(actualTitle, expectedTitle)) {
            return ValidationResult.passed("Found expected title: " + expectedTitle);
        } else {
            return ValidationResult.failed("Title is not as expected",
                    String.format("Expected title: %s", expectedTitle),
                    String.format("Actual title: %s", actualTitle));
        }
    }
}
