package org.goodiemania.melinoe.framework.decorator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import org.goodiemania.melinoe.framework.api.Flow;
import org.goodiemania.melinoe.framework.api.IgnoreFlowDecoration;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.web.BasePage;
import org.goodiemania.melinoe.framework.api.web.By;
import org.goodiemania.melinoe.framework.api.web.ConvertMelinoeBy;
import org.goodiemania.melinoe.framework.api.web.FindElement;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementImpl;
import org.goodiemania.melinoe.framework.drivers.web.page.WebElementListImpl;
import org.goodiemania.melinoe.framework.session.InternalSession;

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
        if (parentClass.isAnnotationPresent(IgnoreFlowDecoration.class)) {
            return;
        }

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
                                throw new MelinoeException(e);
                            }
                        });
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    @SuppressWarnings("java:S3011")
    public Object decorate(final Object object) {
        if (object.getClass().isAnnotationPresent(IgnoreFlowDecoration.class)) {
            return object;
        }

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
                                throw new MelinoeException(e);
                            }
                        });
            }
            currentClass = currentClass.getSuperclass();
        }

        return object;
    }

    public Optional<Object> createObject(Field field) {
        if (field.isAnnotationPresent(IgnoreFlowDecoration.class) || field.getType().isAnnotationPresent(IgnoreFlowDecoration.class)) {
            return Optional.empty();
        }

        if (Flow.class.isAssignableFrom(field.getType())) {
            Class<? extends Flow> type = (Class<? extends Flow>) field.getType();
            return buildFlow(type);
        } else if (BasePage.class.isAssignableFrom(field.getType())) {
            Class<? extends BasePage> type = (Class<? extends BasePage>) field.getType();
            return buildPage(type);
        } else if (field.isAnnotationPresent(FindElement.class)) {
            Optional<By> findBy = buildByFromShortFindBy(field.getAnnotation(FindElement.class));
            if (WebElement.class.isAssignableFrom(field.getType())) {
                return findBy.map(by -> new WebElementImpl(internalSession, by));
            } else if (List.class.isAssignableFrom(field.getType())) {
                return findBy.map(by -> new WebElementListImpl(internalSession,
                        remoteWebDriver -> ConvertMelinoeBy.build(by).findElements(internalSession.getRawWebDriver().getRemoteWebDriver())));
            }
        }

        return Optional.empty();
    }

    public <T extends BasePage> Optional<Object> buildPage(final Class<T> classType) {
        try {
            T value = classType.getConstructor(Session.class).newInstance(internalSession.getSession());
            return Optional.of(value);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new MelinoeException(e);
        }
    }

    public <T extends Flow> Optional<Object> buildFlow(final Class<T> classType) {
        try {
            T value = classType.getConstructor(Session.class).newInstance(internalSession.getSession());
            return Optional.of(value);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new MelinoeException(e);
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
