package org.goodiemania.melinoe.framework.drivers.hecate;

import org.goodiemania.hecate.api.HecateApi;
import org.goodiemania.hecate.confuration.ListenerConfiguration;
import org.goodiemania.melinoe.framework.api.hecate.HecateDriver;
import org.goodiemania.melinoe.framework.api.hecate.HecateListenerDriver;
import org.goodiemania.melinoe.framework.session.InternalSession;

public class HecateDriverImpl implements HecateDriver {

    private final InternalSession internalSession;
    private final HecateApi hecateApi;

    public HecateDriverImpl(final InternalSession internalSession, final String uri) {
        this.internalSession = internalSession;
        this.hecateApi = HecateApi.builder()
                .setBaseUri(uri)
                .build();
    }

    @Override
    public HecateListenerDriver createForListener(final String listenerId) {
        final ListenerConfiguration listenerConfiguration = hecateApi.getListener(listenerId).orElseThrow();

        return new HecateListenerDriverImpl(internalSession, hecateApi, listenerConfiguration);
    }
}
