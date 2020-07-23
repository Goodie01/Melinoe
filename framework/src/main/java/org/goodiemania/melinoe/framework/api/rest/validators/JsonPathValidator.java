package org.goodiemania.melinoe.framework.api.rest.validators;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.ValidationResult;
import org.goodiemania.melinoe.framework.drivers.rest.RestResponse;

public class JsonPathValidator implements RestValidator {
    private final String jsonPathExpression;
    private final String expectedValue;

    public JsonPathValidator(final String jsonPathExpression, final String expectedValue) {
        this.jsonPathExpression = jsonPathExpression;
        this.expectedValue = expectedValue;
    }

    @Override
    public ValidationResult validate(final RestResponse restResponse) {
        String foundValue = JsonPath.parse(restResponse.getResponse()).read(jsonPathExpression, String.class);

        if (StringUtils.equals(foundValue, expectedValue)) {
            return ValidationResult.passed(
                    "Successfully validated JSON path",
                    String.format("JSON Path expression: %s", jsonPathExpression),
                    String.format("Found value: %s", foundValue)
            );
        } else {
            return ValidationResult.failed(
                    "JSON result does not match",
                    String.format("JSON Path expression: %s", jsonPathExpression),
                    String.format("Expected value: %s", expectedValue),
                    String.format("Found value: %s", foundValue)
            );
        }
    }
}
