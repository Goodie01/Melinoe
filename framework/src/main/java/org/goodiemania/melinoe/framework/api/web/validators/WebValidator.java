package org.goodiemania.melinoe.framework.api.web.validators;

import org.goodiemania.melinoe.framework.api.ValidationResult;
import org.goodiemania.melinoe.framework.api.web.WebDriver;

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
    ValidationResult validate(final WebDriver webDriver);
}
