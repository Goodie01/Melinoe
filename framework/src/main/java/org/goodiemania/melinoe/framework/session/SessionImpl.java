package org.goodiemania.melinoe.framework.session;

import java.net.URI;
import java.util.List;
import org.goodiemania.melinoe.framework.api.Session;
import org.goodiemania.melinoe.framework.api.rest.RestRequest;
import org.goodiemania.melinoe.framework.api.web.WebDriver;
import org.goodiemania.melinoe.framework.decorator.FlowDecorator;
import org.goodiemania.melinoe.framework.drivers.rest.RestRequestImpl;
import org.goodiemania.melinoe.framework.session.logging.Logger;
import org.goodiemania.melinoe.framework.validators.Validator;
import org.junit.jupiter.api.Assertions;

public class SessionImpl implements Session {
    private final InternalSession internalSession;

    public SessionImpl(final InternalSession internalSession) {
        this.internalSession = internalSession;
    }


    @Override
    public RestRequest rest(final String uri) {
        return new RestRequestImpl(internalSession.getHttpRequestExecutor(), uri);
    }

    @Override
    public RestRequest rest(final URI uri) {
        return new RestRequestImpl(internalSession.getHttpRequestExecutor(), uri);
    }

    @Override
    public WebDriver web() {
        return internalSession.getRawWebDriver().getWebDriver();
    }

    @Override
    public Logger getLogger() {
        return internalSession.getLogger();
    }

    @Override
    public void decorate(final Object object) {
        new FlowDecorator(internalSession).decorate(object);
    }

    @Override
    public void decorateClass(final Class<?> classType) {
        new FlowDecorator(internalSession).decorateClass(classType);
    }

    @Override
    public Session createSubSession(final String name) {
        final Logger subSessionLogger = internalSession.getClassLogger().createSubSessionLogger(internalSession.getLogger().getMethodName());

        internalSession.getLogger().add()
                .withMessage(name)
                .withSubSessionLogger(subSessionLogger);

        final InternalSession internalSubSession = new InternalSessionImpl(
                this.internalSession.getMetaSession(),
                this.internalSession.getClassLogger(),
                subSessionLogger
        );

        return internalSubSession.getSession();
    }

    @Override
    public void verify(final List<Validator> validators) {
        validators.stream()
                .map(Validator::validate)
                .forEach(validationResult -> validationResult.getMessages().forEach(s -> {
                    if (validationResult.isValid()) {
                        internalSession.getLogger().add()
                                .withMessage(s);
                    } else {
                        internalSession.getLogger().add()
                                .withMessage(s)
                                .fail();
                    }
                }));

        if (!internalSession.getLogger().getHasPassed()) {
            internalSession.getLogger().add()
                    .withMessage("Failure in validation detected. Failing now.")
                    .fail();
            Assertions.fail();
        }
    }
}
