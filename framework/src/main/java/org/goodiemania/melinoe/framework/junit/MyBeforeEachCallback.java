package org.goodiemania.melinoe.framework.junit;

import java.lang.reflect.Field;
import org.goodiemania.melinoe.framework.FlowDecorator;
import org.goodiemania.melinoe.framework.MelinoeTest;
import org.goodiemania.melinoe.framework.Session;

public class MyBeforeEachCallback {
    private MyBeforeEachCallback() {
    }

    public static void callback(final Session session, final MelinoeTest melinoeTest) {
        FlowDecorator flowDecorator = new FlowDecorator(session);
        Class<?> currentClass = melinoeTest.getClass();

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
