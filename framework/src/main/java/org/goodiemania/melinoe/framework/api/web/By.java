package org.goodiemania.melinoe.framework.api.web;

public class By {
    private final ByType type;
    private final String text;

    private By(final ByType type, final String text) {
        this.type = type;
        this.text = text;
    }

    public ByType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public static By id(String id) {
        return new By(ByType.ID, id);
    }

    public static By linkText(String linkText) {
        return new By(ByType.LINK_TEXT, linkText);
    }

    public static By partialLinkText(String partialLinkText) {
        return new By(ByType.PARTIAL_LINK_TEXT, partialLinkText);
    }

    public static By name(String name) {
        return new By(ByType.NAME, name);
    }

    public static By tagName(String tagName) {
        return new By(ByType.TAG_NAME, tagName);
    }

    public static By xpath(String xpathExpression) {
        return new By(ByType.X_PATH, xpathExpression);
    }

    public static By className(String className) {
        return new By(ByType.CLASS_NAME, className);
    }

    public static By cssSelector(String cssSelector) {
        return new By(ByType.CSS_SELECTOR, cssSelector);
    }

    public enum ByType {
        ID,
        LINK_TEXT,
        PARTIAL_LINK_TEXT,
        NAME,
        TAG_NAME,
        X_PATH,
        CLASS_NAME,
        CSS_SELECTOR
    }
}
