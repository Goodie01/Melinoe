package nz.geek.goodwin.melinoe.framework.internal.web.decorator;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.BasePage;
import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.FindElement;
import nz.geek.goodwin.melinoe.framework.api.web.Flow;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * @author Goodie
 */
public class FlowDecorator {
    private Session session;
    public FlowDecorator(Session session) {
        this.session = session;
    }

    @SuppressWarnings("java:S3011")
    public void decorateClass(final Class<?> parentClass) {
        Class<?> currentClass = parentClass;

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
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
        Class<?> currentClass = object.getClass();

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
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
        if (Flow.class.isAssignableFrom(field.getType())) {
            Class<? extends Flow> type = (Class<? extends Flow>) field.getType();
            return buildFlow(type);
        } else if (BasePage.class.isAssignableFrom(field.getType())) {
            Class<? extends BasePage> type = (Class<? extends BasePage>) field.getType();
            return buildPage(type);
        } else if (field.isAnnotationPresent(FindElement.class)) {
            Optional<By> findBy = buildByFromShortFindBy(field.getAnnotation(FindElement.class));

            if (WebElement.class.isAssignableFrom(field.getType())) {
                return findBy.map(by -> session.web().findElement(by));
            } else if (List.class.isAssignableFrom(field.getType())) {
                return findBy.map(by -> session.web().findElements(by));
            }
        }

        return Optional.empty();
    }

    public <T extends BasePage> Optional<Object> buildPage(final Class<T> classType) {
        try {
            T value = classType.getConstructor(Session.class).newInstance(session);
            return Optional.of(value);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new MelinoeException(e);
        }
    }

    public <T extends Flow> Optional<Object> buildFlow(final Class<T> classType) {
        try {
            T value = classType.getConstructor(Session.class).newInstance(session);
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
