package nz.geek.goodwin.melinoe.framework.api;

import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.internal.MotherSession;
import nz.geek.goodwin.melinoe.framework.internal.log.Logger;

/**
 * @author Goodie
 *
 */
public interface Session {
    static Session create() {
        return MotherSession.getInstance().newSession();
    }
    static void closeAll() {
        MotherSession.getInstance().closeAll();
    }
    Session createChildSession(String name);

    WebDriver web();

    Logger log();
}
