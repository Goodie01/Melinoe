package org.goodiemania.melinoe.framework.decorator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.goodiemania.melinoe.framework.api.Flow;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.web.FindElement;
import org.goodiemania.melinoe.framework.drivers.web.BasePage;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementImpl;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.openqa.selenium.By;

/**
 * Created on 2/07/2019.
 */
public class FlowDecorator {
    private InternalSession internalSession;

    public FlowDecorator(final InternalSession internalSession) {
        this.internalSession = internalSession;
    }

    @SuppressWarnings("java:S3011")
    public void decorate(final Class<?> parentClass) {
        Class<?> currentClass = parentClass;

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    break;
                }
                createObject(field)
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

    public Object decorate(final Object object) {
        Class<?> currentClass = object.getClass();

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    break;
                }
                createObject(field)
                        .ifPresent(o -> {
                            try {
                                field.setAccessible(true);
                                field.set(object, o);
                            } catch (IllegalAccessException e) {
                                throw new IllegalStateException(e);
                            }
                        });
            }
            currentClass = currentClass.getSuperclass();
        }

        return object;
    }

    public Optional<Object> createObject(Field field) {
        if (Flow.class.isAssignableFrom(field.getType())) {
            Class<? extends Flow> type = (Class<? extends Flow>) field.getType();
            return Optional.of(buildFlow(type))
                    .map(this::decorate);
        } else if (BasePage.class.isAssignableFrom(field.getType())) {
            Class<? extends BasePage> type = (Class<? extends BasePage>) field.getType();
            return Optional.of(buildPage(type))
                    .map(this::decorate);
        } else if (field.isAnnotationPresent(FindElement.class)) {
            FindElement annotation = field.getAnnotation(FindElement.class);
            return buildByFromShortFindBy(annotation)
                    .map((By by) -> new WebElementImpl(internalSession, by));
        }

        return Optional.empty();
    }

    public <T extends BasePage> T buildPage(final Class<T> classType) {
        try {
            return classType.getConstructor(Session.class).newInstance(internalSession.getSession());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T extends Flow> T buildFlow(final Class<T> classType) {
        try {
            return classType.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }


    private Optional<By> buildByFromShortFindBy(FindElement findElement) {
        if (!"".equals(findElement.className())) {
            return Optional.of(By.className(findElement.className()));
        }

        if (!"".equals(findElement.css())) {
            return Optional.of(By.cssSelector(findElement.css()));
        }

        if (!"".equals(findElement.id())) {
            return Optional.of(By.id(findElement.id()));
        }

        if (!"".equals(findElement.linkText())) {
            return Optional.of(By.linkText(findElement.linkText()));
        }

        if (!"".equals(findElement.name())) {
            return Optional.of(By.name(findElement.name()));
        }

        if (!"".equals(findElement.partialLinkText())) {
            return Optional.of(By.partialLinkText(findElement.partialLinkText()));
        }

        if (!"".equals(findElement.tagName())) {
            return Optional.of(By.tagName(findElement.tagName()));
        }

        if (!"".equals(findElement.xpath())) {
            return Optional.of(By.xpath(findElement.xpath()));
        }

        // Fall through
        return Optional.empty();
    }

}
