package org.goodiemania.melinoe.framework;

import java.lang.reflect.Field;
import java.util.Optional;
import org.goodiemania.melinoe.framework.web.BasePage;

/**
 * Created on 2/07/2019.
 */
public class FlowDecorator {
    private Session session;

    public FlowDecorator(final Session session) {
        this.session = session;
    }

    public Optional<Object> decorate(Field field) {
        if (BasePage.class.isAssignableFrom(field.getType())) {
            Class<? extends BasePage> type = (Class<? extends BasePage>) field.getType();
            return Optional.of(session.web().buildPage(type));
        }

        return Optional.empty();
    }

}
