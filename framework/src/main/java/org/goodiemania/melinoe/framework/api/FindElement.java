package org.goodiemania.melinoe.framework.api;

public @interface FindElement {  String id() default "";
    String name() default "";

    String className() default "";

    String css() default "";

    String tagName() default "";

    String linkText() default "";

    String partialLinkText() default "";

    String xpath() default "";
}
