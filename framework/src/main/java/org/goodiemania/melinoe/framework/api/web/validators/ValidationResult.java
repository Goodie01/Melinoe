package org.goodiemania.melinoe.framework.api.web.validators;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 26/06/2019.
 */
public class ValidationResult {
    private final boolean valid;
    private final List<String> messages;

    public ValidationResult(final boolean valid, final List<String> messages) {
        this.valid = valid;
        this.messages = messages;
    }

    public static ValidationResult passed(final String... messages) {
        return new ValidationResult(true, Arrays.asList(messages));
    }

    public static ValidationResult failed(final String... messages) {
        return new ValidationResult(false, Arrays.asList(messages));
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getMessages() {
        return messages;
    }
}
