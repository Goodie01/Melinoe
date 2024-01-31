package nz.geek.goodwin.melinoe.framework.api;

import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.internal.MotherSession;
import nz.geek.goodwin.melinoe.framework.internal.log.Logger;

/**
 * @author Goodie
 *
 */
public interface Session {
    static Session get() {
        return MotherSession.getInstance().get();
    }
    static void close() {
        MotherSession.getInstance().close();
    }
    Session createChildSession(String name);

    WebDriver web();

    Logger log();
}
