package org.goodiemania.melinoe.framework.api.rest.validators;

import org.goodiemania.melinoe.framework.api.ValidationResult;
import org.goodiemania.melinoe.framework.drivers.rest.RestResponse;

public interface RestValidator {
    /**
     * Attempts to validate the given rest result
     *
     * @param restResponse Rest response, with which to make calls to determine validation satus
     * @return validation result, indicating if it succeeded or not, and any associated error messages
     */
    ValidationResult validate(final RestResponse restResponse);
}
