package org.goodiemania.melinoe.framework.drivers.hecate;

import java.util.List;
import java.util.stream.Collectors;
import org.goodiemania.hecate.api.HecateApi;
import org.goodiemania.hecate.confuration.ListenerConfiguration;
import org.goodiemania.hecate.logs.Log;
import org.goodiemania.melinoe.framework.api.hecate.HecateListenerDriver;
import org.goodiemania.melinoe.framework.api.hecate.validators.HecateLogListValidator;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.validators.Validator;

public class HecateListenerDriverImpl implements HecateListenerDriver {
    private final InternalSession internalSession;
    private final HecateApi hecateApi;
    private final ListenerConfiguration listenerConfiguration;


    @Override
    public void deleteListener() {
        hecateApi.deleteListener(listenerConfiguration.getId());
    }

    public HecateListenerDriverImpl(final InternalSession internalSession,
                                    final HecateApi hecateApi,
                                    final ListenerConfiguration listenerConfiguration) {
        this.internalSession = internalSession;
        this.hecateApi = hecateApi;
        this.listenerConfiguration = listenerConfiguration;
    }

    @Override
    public void verify(final List<HecateLogListValidator> validators) {
        internalSession.getSession().getLogger().add().withMessage("Validating listener logs");

        final List<Log> allLogsForListener = hecateApi.getAllLogsForListener(listenerConfiguration.getId());

        final List<Validator> collect = validators.stream()
                .map(webValidator -> (Validator) () -> webValidator.validate(allLogsForListener))
                .collect(Collectors.toList());

        internalSession.getSession().verify(collect);
    }
}
