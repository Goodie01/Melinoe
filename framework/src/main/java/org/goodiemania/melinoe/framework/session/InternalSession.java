package org.goodiemania.melinoe.framework.session;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;

public interface InternalSession {
    Session getSession();

    FlowDecorator getFlowDecorator();

    RawWebDriver getRawWebDriver();
}
