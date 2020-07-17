package org.goodiemania.melinoe.framework.api;

import org.goodiemania.melinoe.framework.session.InternalSessionClassImpl;
import org.goodiemania.melinoe.framework.session.InternalSessionImpl;
import org.goodiemania.melinoe.framework.session.MetaSession;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class MelinoeTest {
    private static final MetaSession metaSession = new MetaSession();
    private static InternalSessionClassImpl classSession;

    @RegisterExtension
    static BeforeAllCallback beforeAllCallback = extensionContext -> {
        Class<?> testClass = extensionContext.getTestClass().orElseThrow();

        classSession = metaSession.createSessionFor(extensionContext);
        classSession.getFlowDecorator().decorate(testClass);
    };

    @RegisterExtension
    static AfterAllCallback afterAllCallback = extensionContext -> {
        metaSession.endSession();
        metaSession.writeLogs();
    };

    private InternalSessionImpl session;

    @RegisterExtension
    BeforeEachCallback beforeEachCallback = extensionContext -> {
        session = classSession.createTestSession(extensionContext);
        session.getFlowDecorator().decorate(this);
    };

    @RegisterExtension
    AfterEachCallback afterEachCallback = extensionContext -> {
        extensionContext.getExecutionException()
                .ifPresent(throwable -> {
                    session.getSession().getLogger().add("Exception detected in after each callback");
                    session.getSession().getLogger().fail();
                });
        metaSession.endSession();
    };

    public static Session getClassSession() {
        return classSession.getSession();
    }

    public Session getSession() {
        return session.getSession();
    }
}
