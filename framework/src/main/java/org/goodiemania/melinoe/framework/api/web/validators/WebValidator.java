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
     * @param webDriver Web driver, with which to make calls to determine validation status
     * @return validation result, indicating if it succeeded or not, and any associated error messages
     */
    ValidationResult validate(final WebDriver webDriver);
}
