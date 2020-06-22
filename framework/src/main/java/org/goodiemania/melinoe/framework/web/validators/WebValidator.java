package org.goodiemania.melinoe.framework.web.validators;

import org.goodiemania.melinoe.framework.Session;

/**
 * Created on 26/06/2019.
 */
public interface WebValidator {
    /**
     * Attempts to validate the current web page state
     *
     * @param context   current context for this test
     * @param webDriver private driver from within the context itself
     * @return validation result, indicating if it succeeded or not, and any associated error messages
     */
    ValidationResult validate(final Session context, final org.openqa.selenium.WebDriver webDriver);
}
