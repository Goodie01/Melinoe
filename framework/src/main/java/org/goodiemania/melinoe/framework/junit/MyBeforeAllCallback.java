package org.goodiemania.melinoe.framework.junit;

import java.lang.reflect.Field;
import org.goodiemania.melinoe.framework.FlowDecorator;
import org.goodiemania.melinoe.framework.Session;

public class MyBeforeAllCallback {
    private MyBeforeAllCallback() {
    }

    public static void callBack(final Session session, final Class<?> parentClass) throws Exception {
        FlowDecorator flowDecorator = new FlowDecorator(session);
        Class<?> currentClass = parentClass;

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
    }
}
