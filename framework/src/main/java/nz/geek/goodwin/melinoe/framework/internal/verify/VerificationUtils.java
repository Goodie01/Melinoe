package nz.geek.goodwin.melinoe.framework.internal.verify;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Goodie
 */
public final class VerificationUtils {
    private VerificationUtils() {
    }

    public static void checkVerificationResults(List<ValidationResult> list) {
        if(list.stream().anyMatch(validationResult -> !validationResult.valid())) {
            var failedVerificationMessages = list.stream()
                    .filter(validationResult -> !validationResult.valid())
                    .map(validationResult1 -> String.join(", ", validationResult1.messages()))
                    .collect(Collectors.joining(System.lineSeparator()));
            throw new MelinoeException("Verification failed" + System.lineSeparator() + failedVerificationMessages);
        }
    }
}
