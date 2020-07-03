package org.goodiemania.melinoe.framework.junit;

import java.lang.reflect.Field;
import org.goodiemania.melinoe.framework.InternalSession;

public class MyBeforeAllCallback {
    private MyBeforeAllCallback() {
    }

    public static void callBack(final InternalSession session, final Class<?> parentClass) throws Exception {
        Class<?> currentClass = parentClass;

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    break;
                }
                session.getFlowDecorator().decorate(field)
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
