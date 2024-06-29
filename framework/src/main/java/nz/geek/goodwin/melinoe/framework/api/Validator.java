package nz.geek.goodwin.melinoe.framework.api;

import nz.geek.goodwin.melinoe.framework.api.rest.HttpResult;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;

/**
 * @author Goodie
 */
public interface Validator<T> {
    ValidationResult validate(final T result);
}
