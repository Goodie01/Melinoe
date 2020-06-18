package org.goodiemania.melinoe.framework;

import java.util.Objects;
import org.goodiemania.melinoe.framework.session.MetaSession;
import org.goodiemania.melinoe.framework.web.WebDriver;
import org.goodiemania.melinoe.framework.web.WebDriverGenerator;

public class SessionImpl implements Session {
    private WebDriver webDriver;
    private MetaSession metaSession;

    public SessionImpl(final MetaSession metaSession) {
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
}
