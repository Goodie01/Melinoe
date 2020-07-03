package org.goodiemania.melinoe.framework.junit;

import java.lang.reflect.Field;
import org.goodiemania.melinoe.framework.InternalSession;
import org.goodiemania.melinoe.framework.api.MelinoeTest;

public class MyBeforeEachCallback {
    private MyBeforeEachCallback() {
    }

    public static void callback(final InternalSession session, final MelinoeTest melinoeTest) {
        Class<?> currentClass = melinoeTest.getClass();

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    break;
                }
                session.getFlowDecorator().decorate(field)
                        .ifPresent(o -> {
                            try {
                                field.setAccessible(true);
                                field.set(melinoeTest, o);
                            } catch (IllegalAccessException e) {
                                throw new IllegalStateException(e);
                            }
                        });
            }
            currentClass = currentClass.getSuperclass();
        }
    }
}
