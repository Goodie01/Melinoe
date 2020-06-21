package org.goodiemania.melinoe.framework;

import java.lang.reflect.Field;
import java.util.Optional;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
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
        classSession = metaSession.createSessionFor(extensionContext);

        Class<?> currentClass = new Object() {
        }.getClass().getEnclosingClass();
        FlowDecorator flowDecorator = new FlowDecorator(classSession);

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    break;
                }
                flowDecorator.decorate(field)
                        .ifPresent(o -> {
                            try {
                                field.setAccessible(true);
                                field.set(null, o);
                            } catch (IllegalAccessException e) {
                                throw new IllegalStateException(e);
                            }
                        });
            }
            currentClass = currentClass.getSuperclass();
        }
    };

    @RegisterExtension
    static AfterAllCallback afterAllCallback = extensionContext -> {
        metaSession.closeAllDrivers();
        metaSession.logStuff();
    };

    private Session session;

    @RegisterExtension
    BeforeEachCallback beforeEachCallback = extensionContext -> {
        session = metaSession.createSessionFor(extensionContext);
        Class<?> currentClass = this.getClass();
        FlowDecorator flowDecorator = new FlowDecorator(session);

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    break;
                }
                flowDecorator.decorate(field)
                        .ifPresent(o -> {
                            try {
                                field.setAccessible(true);
                                field.set(this, o);
                            } catch (IllegalAccessException e) {
                                throw new IllegalStateException(e);
                            }
                        });
            }
            currentClass = currentClass.getSuperclass();
        }
    };
    @RegisterExtension
    AfterEachCallback afterEachCallback = extensionContext -> {
        metaSession.closeAllDrivers();
    };

    //we want to do some funky stuff here...
    public static Session getClassSession() {
        return classSession;
    }

    public Session getSession() {
        return session;
    }
}
