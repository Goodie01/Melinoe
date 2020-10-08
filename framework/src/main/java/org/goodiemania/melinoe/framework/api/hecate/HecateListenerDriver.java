package org.goodiemania.melinoe.framework.api.hecate;

import java.util.Arrays;
import java.util.List;
import org.goodiemania.melinoe.framework.api.hecate.validators.HecateLogListValidator;

public interface HecateListenerDriver {
    void verify(final List<HecateLogListValidator> validators);

    default void verify(final HecateLogListValidator... validators) {
        verify(Arrays.asList(validators));
    }
}
