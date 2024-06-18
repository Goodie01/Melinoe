package nz.geek.goodwin.melinoe.framework.api;

import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.rest.RestDriver;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import nz.geek.goodwin.melinoe.framework.internal.MotherSession;

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

    RestDriver rest();

    Logger log();
}
