package org.goodiemania.melinoe.framework.api.hecate.validators;

import java.util.List;
import org.goodiemania.hecate.logs.Log;
import org.goodiemania.melinoe.framework.api.ValidationResult;

public interface HecateLogListValidator {
    ValidationResult validate(final List<Log> log);
}
