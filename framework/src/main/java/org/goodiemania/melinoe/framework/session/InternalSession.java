package org.goodiemania.melinoe.framework.session;

import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.drivers.rest.HttpRequestExecutor;
import org.goodiemania.melinoe.framework.drivers.web.RawWebDriver;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.session.logging.Logger;

public interface InternalSession {
    MetaSession getMetaSession();

    ClassLogger getClassLogger();

    Session getSession();

    RawWebDriver getRawWebDriver();

    HttpRequestExecutor getHttpRequestExecutor();

    Logger getLogger();
}
