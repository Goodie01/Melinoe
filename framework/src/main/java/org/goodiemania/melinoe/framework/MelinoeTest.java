package org.goodiemania.melinoe.framework;

import org.goodiemania.melinoe.framework.junit.MyBeforeAllCallback;
import org.goodiemania.melinoe.framework.junit.MyBeforeEachCallback;
import org.goodiemania.melinoe.framework.session.MetaSession;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class MelinoeTest {
    private static final MetaSession metaSession = new MetaSession();
    private static Session classSession;

    @RegisterExtension
    static BeforeAllCallback beforeAllCallback = extensionContext -> {
        Class<?> currentClass = new Object() {
        }.getClass().getEnclosingClass();

        classSession = metaSession.createSessionFor(extensionContext);
        MyBeforeAllCallback.callBack(classSession, currentClass);
    };

    @RegisterExtension
    static AfterAllCallback afterAllCallback = extensionContext -> {
        metaSession.endSession();
        metaSession.logStuff();
    };

    private Session session;

    @RegisterExtension
    BeforeEachCallback beforeEachCallback = extensionContext -> {
        session = metaSession.createSessionFor(extensionContext);
        MyBeforeEachCallback.callback(session, this);
    };

    @RegisterExtension
    AfterEachCallback afterEachCallback = extensionContext -> {
        metaSession.endSession();
    };

    public static Session getClassSession() {
        return classSession;
    }

    public Session getSession() {
        return session;
    }
}
