package org.goodiemania.melinoe.framework.drivers.hecate;

import java.util.Collections;
import java.util.function.BooleanSupplier;
import org.goodiemania.hecate.api.HecateApi;
import org.goodiemania.hecate.confuration.ListenerConfiguration;
import org.goodiemania.melinoe.framework.api.hecate.HecateDriver;
import org.goodiemania.melinoe.framework.api.hecate.HecateListenerDriver;
import org.goodiemania.melinoe.framework.session.InternalSession;

public class HecateDriverImpl implements HecateDriver {

    private final InternalSession internalSession;
    private final HecateApi hecateApi;

    public HecateDriverImpl(final InternalSession internalSession, final String host, final int adminPort) {
        this.internalSession = internalSession;
        this.hecateApi = HecateApi.builder()
                .setBaseUri("http://" + host + ":" + adminPort)
                .build();
    }

    @Override
    public HecateListenerDriver createNewListener(final String listenerId, final String httpMethod, final int port, final String path) {
        final ListenerConfiguration listenerConfiguration = new ListenerConfiguration();
        listenerConfiguration.setId(listenerId);
        listenerConfiguration.setHttpMethod(httpMethod);
        listenerConfiguration.setPort(port);
        listenerConfiguration.setContext(path);
        listenerConfiguration.setRules(Collections.emptyMap());

        hecateApi.writeListener(listenerConfiguration);

        waitUntil(() -> hecateApi.checkHealthCheck().equals("OK"), 600_000);

        return forListener(listenerId);
    }

    @Override
    public HecateListenerDriver forListener(final String listenerId) {
        final ListenerConfiguration listenerConfiguration = hecateApi.getListener(listenerId).orElseThrow();

        return new HecateListenerDriverImpl(internalSession, hecateApi, listenerConfiguration);
    }

    private static void waitUntil(BooleanSupplier condition, long timeoutms) {
        long start = System.currentTimeMillis();
        while (!condition.getAsBoolean()) {
            if (System.currentTimeMillis() - start > timeoutms) {
                throw new IllegalStateException("Timeout while waiting for condition");
            }
        }
    }
}
