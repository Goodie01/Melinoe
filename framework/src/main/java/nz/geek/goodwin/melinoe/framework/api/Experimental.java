package nz.geek.goodwin.melinoe.framework.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Goodie
 */


@Retention(RetentionPolicy.SOURCE)
public @interface Experimental {
    String value() default "";
}
