package nz.geek.goodwin.melinoe.framework.internal.verify;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.Validator;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;
import nz.geek.goodwin.melinoe.framework.internal.Configuration;
import nz.geek.goodwin.melinoe.framework.internal.misc.Sleeper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Goodie
 */
public final class VerificationUtils {
    private VerificationUtils() {
    }

    public static <T> T validate(List<? extends Validator<T>> validators, Logger logger, Supplier<T> thingSupplier) {
        List<ValidationResult> results;
        int retryCount = 0;
        T thing;

        do {
            if (retryCount != 0) {
                Sleeper.sleep(Configuration.RETRY_SLEEP_TIME_MS.intVal());
            }

            retryCount++;
            thing = thingSupplier.get();
            List<ValidationResult> list = new ArrayList<>();
            for (Validator<T> webValidator : validators) {
                list.add(webValidator.validate(thing));
            }
            results = list;
        } while (results.stream().anyMatch(validationResult -> !validationResult.valid()) && retryCount < Configuration.RETRY_COUNT.intVal());

        final int tryCount = retryCount;

        results.forEach(validationResult -> {
            String messages = validationResult.messages().stream().collect(Collectors.joining(System.lineSeparator()));
            logger.add().withMessage(messages).withSuccess(validationResult.valid());
        });

        if(results.stream().anyMatch(validationResult -> !validationResult.valid())) {
            throw new MelinoeException("Verification failed after " + tryCount + " attempts");
        } else if (tryCount != 1){
            logger.add().withMessage("Verification successful after " + tryCount + "attempts");
        } else {
            logger.add().withMessage("Verification successful");

        }

        VerificationUtils.checkVerificationResults(results);
        return thing;
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
