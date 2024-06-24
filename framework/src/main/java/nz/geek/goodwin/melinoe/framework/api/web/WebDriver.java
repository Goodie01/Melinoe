package nz.geek.goodwin.melinoe.framework.api.web;

import nz.geek.goodwin.melinoe.framework.api.Experimental;
import nz.geek.goodwin.melinoe.framework.api.web.validation.WebValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Goodie
 */
public interface WebDriver {
    void decorate(Object object);

    void decorateClass(Class<?> classType);

    Navigate navigate();

    String getTitle();

    void waitFor(Predicate<WebDriver> predicate);

    WebElement findElement(By by);

    List<WebElement> findElements(By by);

    @Experimental
    void clearCookie(String name);

    @Experimental
    void clearCookie(Cookie cookie);

    @Experimental
    void clearAll();
    @Experimental
    Map<String, Cookie> cookies();

    void verify(final List<WebValidator> validators);

    default void verify(final WebValidator... validators) {
        verify(Arrays.asList(validators));
    }
}
