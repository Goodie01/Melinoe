package nz.geek.goodwin.melinoe.framework.api.rest.validation;

import nz.geek.goodwin.melinoe.framework.api.Validator;
import nz.geek.goodwin.melinoe.framework.api.rest.HttpResult;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;

/**
 * @author Goodie
 */
public interface RestValidator<T> extends Validator<HttpResult<T>> {
    ValidationResult validate(final HttpResult<T> result);

    static <T> RestValidator<T> statusCodeEquals(int statusCode) {
        return result -> {
            int respStatus = result.respStatus();
            return respStatus == statusCode
                    ? ValidationResult.passed("Found expected status code: " + statusCode)
                    : ValidationResult.failed(
                    "Status code is not as expected",
                    "Expected status: " + statusCode,
                    "Actual status: " + respStatus
            );
        };
    }
}
