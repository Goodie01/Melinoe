package nz.geek.goodwin.melinoe.framework.api.web.validation;

import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;

/**
 * @author Goodie
 */
public interface WebValidator {
    ValidationResult validate(final WebDriver webDriver);
}
