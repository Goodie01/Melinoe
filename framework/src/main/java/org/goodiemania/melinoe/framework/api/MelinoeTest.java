package org.goodiemania.melinoe.framework.api;

import org.goodiemania.melinoe.framework.session.InternalSession;
import org.goodiemania.melinoe.framework.session.InternalSessionClassImpl;
import org.goodiemania.melinoe.framework.session.MetaSession;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class MelinoeTest {
    private static final MetaSession metaSession = new MetaSession();
    private static InternalSessionClassImpl internalClassSession;
    private static boolean afterAll = false;

    @RegisterExtension
    static BeforeAllCallback beforeAllCallback = extensionContext -> {
        Class<?> testClass = extensionContext.getTestClass().orElseThrow();

        internalClassSession = metaSession.createSessionFor(extensionContext);
        internalClassSession.getFlowDecorator().decorate(testClass);
    };

    @RegisterExtension
    static AfterAllCallback afterAllCallback = extensionContext -> {
        extensionContext.getExecutionException()
                .ifPresent(throwable -> internalClassSession.getSession().getLogger().add()
                        .fail()
                        .withMessage("Exception thrown")
                        .withThrowable(throwable));

        metaSession.endSession();
        metaSession.writeLogs();
    };

    private InternalSession internalSession;

    @RegisterExtension
    BeforeEachCallback beforeEachCallback = extensionContext -> {
        internalSession = internalClassSession.createTestSession(extensionContext);
        internalSession.getFlowDecorator().decorate(this);
        afterAll = true;
    };

    @RegisterExtension
    AfterEachCallback afterEachCallback = extensionContext -> {
        extensionContext.getExecutionException()
                .ifPresent(throwable -> internalSession.getSession().getLogger().add()
                        .fail()
                        .withMessage("Exception thrown")
                        .withThrowable(throwable));
        metaSession.endSession();
    };

    public static Session getClassSession() {
        if (afterAll) {
            internalClassSession.resetLoggerToAfterAll();
        }
        return internalClassSession.getSession();
    }

    public Session getSession() {
        return internalSession.getSession();
    }
}
