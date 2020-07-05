package org.goodiemania.melinoe.framework.junit;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.framework.session.InternalSession;

public class MyBeforeEachCallback {
    private MyBeforeEachCallback() {
    }

    public static void callback(final InternalSession session, final MelinoeTest melinoeTest) {
        session.getFlowDecorator().decorate(melinoeTest);
    }
}
