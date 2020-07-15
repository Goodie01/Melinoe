package org.goodiemania.melinoe.framework.api.exceptions;

public class MelinoeException extends RuntimeException {
    public MelinoeException(final String message) {
        super(message);
    }

    public MelinoeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MelinoeException(final Throwable cause) {
        super(cause);
    }
}
