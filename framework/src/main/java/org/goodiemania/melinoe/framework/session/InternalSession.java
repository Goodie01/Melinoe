package org.goodiemania.melinoe.framework.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;

public interface InternalSession {
    MetaSession getMetaSession();

    Session getSession();

    FlowDecorator getFlowDecorator();

    RawWebDriver getRawWebDriver();

    ObjectMapper getObjectMapper();

    HttpClient getHttpClient();

    ClassLogger getClassLogger();
}
