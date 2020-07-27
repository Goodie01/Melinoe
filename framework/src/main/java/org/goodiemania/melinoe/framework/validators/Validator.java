package org.goodiemania.melinoe.framework.validators;

import org.goodiemania.melinoe.framework.api.ValidationResult;

public interface Validator {
    /**
     * @return validation result, indicating if it succeeded or not, and any associated error messages
     */
    ValidationResult validate();
}
