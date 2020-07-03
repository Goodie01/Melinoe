package org.goodiemania.melinoe.framework;

import java.lang.reflect.Field;
import java.util.Optional;
import org.goodiemania.melinoe.framework.api.FindElement;
import org.goodiemania.melinoe.framework.web.BasePage;
import org.goodiemania.melinoe.framework.web.RawWebDriver;
import org.openqa.selenium.By;

/**
 * Created on 2/07/2019.
 */
public class FlowDecorator {
    private RawWebDriver rawWebDriver;

    public FlowDecorator(final RawWebDriver rawWebDriver) {
        this.rawWebDriver = rawWebDriver;
    }

    public Optional<Object> decorate(Field field) {
        if (BasePage.class.isAssignableFrom(field.getType())) {
            Class<? extends BasePage> type = (Class<? extends BasePage>) field.getType();
            return Optional.of(rawWebDriver.buildPage(type));
        } else if (field.isAnnotationPresent(FindElement.class)) {
            FindElement annotation = field.getAnnotation(FindElement.class);
            buildByFromShortFindBy(annotation)
            .map(by -> rawWebDriver.remoteWebDriver().findElement(by))
            .map(webElement -> )
        }

        return Optional.empty();
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
