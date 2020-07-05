package org.goodiemania.melinoe.framework.junit;

import org.goodiemania.melinoe.framework.session.InternalSession;

public class MyBeforeAllCallback {
    private MyBeforeAllCallback() {
    }

    public static void callBack(final InternalSession session, final Class<?> parentClass) throws Exception {
        session.getFlowDecorator().decorate(parentClass);
    }
}
