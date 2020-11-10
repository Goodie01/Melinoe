package org.goodiemania.melinoe.framework.validators;

import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.ValidationResult;

public class ContainsValidator implements Validator {
    private final Supplier<String> providedTextSupplier;
    private final String searchText;

    public ContainsValidator(final Supplier<String> providedTextSupplier, final String searchText) {
        this.providedTextSupplier = providedTextSupplier;
        this.searchText = searchText;
    }

    @Override
    public ValidationResult validate() {
        String providedText = providedTextSupplier.get();

        if (StringUtils.contains(providedText, searchText)) {
            return ValidationResult.passed("Found expected text: " + searchText,
                    "Actual text: " + providedText);
        } else {
            return ValidationResult.failed("Could not find expected text: " + searchText,
                    "Actual text: " + providedText);
        }
    }
}
