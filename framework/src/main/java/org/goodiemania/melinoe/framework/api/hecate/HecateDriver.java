package org.goodiemania.melinoe.framework.api.hecate;

public interface HecateDriver {
    HecateListenerDriver createForListener(final String listenerId);
}
