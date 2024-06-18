package nz.geek.goodwin.melinoe.framework.api;

/**
 * This is our root (runtime) exception.
 * <p>
 * If we throw anything any (internal) errors it should extend this. For ex, being unable to convert a rest response to a object.
 */
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
