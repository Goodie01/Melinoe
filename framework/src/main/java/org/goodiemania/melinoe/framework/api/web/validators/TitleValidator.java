package org.goodiemania.melinoe.framework.api.web.validators;

import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.web.WebDriver;

/**
 * Created on 28/06/2019.
 */
public class TitleValidator implements WebValidator {
    private final String expectedTitle;

    public TitleValidator(final String expectedTitle) {
        this.expectedTitle = expectedTitle;
    }

    @Override
    public ValidationResult validate(final Session context, final WebDriver webDriver) {
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
