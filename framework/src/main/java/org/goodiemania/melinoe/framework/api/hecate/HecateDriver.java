package org.goodiemania.melinoe.framework.api.hecate;

public interface HecateDriver {
    HecateListenerDriver createNewListener(final String listenerId, final String httpMethod, final int port, final String path);

    HecateListenerDriver forListener(final String listenerId);
}
