package org.goodiemania.melinoe.framework.api.hecate.validators;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.goodiemania.hecate.logs.Log;
import org.goodiemania.melinoe.framework.api.ValidationResult;

public class LogResponseContainsListValidator implements HecateLogListValidator {
    private Predicate<Log> logPredicate;

    public LogResponseContainsListValidator(final Predicate<Log> logPredicate) {
        this.logPredicate = logPredicate;
    }

    @Override
    public ValidationResult validate(final List<Log> logs) {
        final Optional<Log> foundLog = logs.stream()
                .filter(logPredicate)
                .findFirst();

        if (foundLog.isPresent()) {
            final Log log = foundLog.get();
            return ValidationResult.passed("Predicate matched for request",
                    "Request path:" + log.getRequest().getPath(),
                    "Request method" + log.getRequest().getHttpMethod(),
                    "Request body" + log.getRequest().getBody(),
                    "Response body" + log.getResponse().getBody()
            );
        } else {
            return ValidationResult.failed("Could not find find a log to match predicate");
        }
    }
}
