package org.goodiemania.melinoe.framework.api.rest.validators;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.ValidationResult;
import org.goodiemania.melinoe.framework.drivers.rest.RestResponse;

public interface RestValidator {
    ValidationResult validate(final Session context, final RestResponse webDriver);
}
