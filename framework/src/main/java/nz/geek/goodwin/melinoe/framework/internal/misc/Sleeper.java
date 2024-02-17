package nz.geek.goodwin.melinoe.framework.internal.misc;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;

/**
 * @author Goodie
 */
public final class Sleeper {
    private Sleeper() {
    }

    public static void sleep(final int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new MelinoeException(e);
        }
    }
}
