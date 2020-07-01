package org.goodiemania.melinoe.framework;

import java.util.Objects;
import org.goodiemania.melinoe.framework.session.MetaSession;
import org.goodiemania.melinoe.framework.session.logging.ClassLogger;
import org.goodiemania.melinoe.framework.web.WebDriver;
import org.goodiemania.melinoe.framework.web.WebDriverGenerator;
import org.goodiemania.melinoe.framework.web.WebDriverImpl;

public class SessionImpl implements Session {
    private final MetaSession metaSession;
    private final ClassLogger classLogger;

    private WebDriverImpl webDriver;

    public SessionImpl(final MetaSession metaSession, final ClassLogger classLogger) {
        this.classLogger = classLogger;
        Objects.requireNonNull(metaSession, "Meta Session is null");
        this.metaSession = metaSession;
    }

    @Override
    public void rest() {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public WebDriver web() {
        if (webDriver == null) {
            webDriver = new WebDriverGenerator().generate(this);
            metaSession.addDriver(webDriver);
        }
        return webDriver;
    }

    @Override
    public ClassLogger getLogger() {
        return classLogger;
    }
}
