package nz.geek.goodwin.melinoe.framework.api.web.validation;

import java.util.Arrays;
import java.util.List;

/**
 * @author Goodie
 */
public record ValidationResult(boolean valid, List<String> messages) {

    public static ValidationResult  passed(final String... messages) {
        return new ValidationResult(true, Arrays.asList(messages));
    }

    public static ValidationResult failed(final String... messages) {
        return new ValidationResult(false, Arrays.asList(messages));
    }
}
