package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.web.By;
import nz.geek.goodwin.melinoe.framework.api.web.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class WebElementListImpl implements List<WebElement> {

    private final RemoteWebDriver remoteWebDriver;
    private final Function<RemoteWebDriver, List<WebElement>> webElementSupplier;

    public WebElementListImpl(RemoteWebDriver remoteWebDriver, ScreenshotTaker screenshotTaker, PageCheckStatus pageCheckStatus, Logger logger, By by) {
        this.remoteWebDriver = remoteWebDriver;
        this.webElementSupplier = webDriver -> webDriver.findElements(ConvertBy.build(by))
                .stream()
                .map(webElement -> (WebElement) new WebElementImpl(remoteWebDriver, screenshotTaker, pageCheckStatus, logger, by, remoteWebDriver1 -> webElement))
                .toList();
    }

    public WebElementListImpl(RemoteWebDriver remoteWebDriver, ScreenshotTaker screenshotTaker, PageCheckStatus pageCheckStatus, Logger logger, Function<RemoteWebDriver, org.openqa.selenium.WebElement> parentSupplier, By by) {
        this.remoteWebDriver = remoteWebDriver;
        this.webElementSupplier = webDriver -> parentSupplier.apply(webDriver).findElements(ConvertBy.build(by))
                .stream()
                .map(webElement -> (WebElement) new WebElementImpl(remoteWebDriver, screenshotTaker, pageCheckStatus, logger, by, remoteWebDriver1 -> webElement))
                .toList();
    }

    private List<WebElement> getElementList() {
        return webElementSupplier.apply(remoteWebDriver);
    }

    @Override
    public int size() {
        return getElementList().size();
    }

    @Override
    public boolean isEmpty() {
        return getElementList().isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return getElementList().contains(o);
    }


    @Override
    public Iterator<WebElement> iterator() {
        return getElementList().iterator();
    }


    @Override
    public Object[] toArray() {
        return getElementList().toArray();
    }


    @Override
    public <T> T[] toArray(final T[] ts) {
        return getElementList().toArray(ts);
    }

    @Override
    public <T> T[] toArray(final IntFunction<T[]> generator) {
        return getElementList().toArray(generator);
    }

    @Override
    public boolean add(final WebElement webElement) {
        return getElementList().add(webElement);
    }

    @Override
    public void add(final int i, final WebElement webElement) {
        getElementList().add(i, webElement);
    }

    @Override
    public boolean remove(final Object o) {
        return getElementList().remove(o);
    }

    @Override
    public WebElement remove(final int i) {
        return getElementList().remove(i);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return getElementList().containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends WebElement> collection) {
        return getElementList().addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends WebElement> collection) {
        return getElementList().addAll(i, collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return getElementList().removeAll(collection);
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return getElementList().retainAll(collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<WebElement> operator) {
        getElementList().replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super WebElement> c) {
        getElementList().sort(c);
    }

    @Override
    public void clear() {
        getElementList().clear();
    }

    @Override
    public boolean equals(final Object o) {
        return getElementList().equals(o);
    }

    @Override
    public int hashCode() {
        return getElementList().hashCode();
    }

    @Override
    public WebElement get(final int i) {
        return getElementList().get(i);
    }

    @Override
    public WebElement set(final int i, final WebElement webElement) {
        return getElementList().set(i, webElement);
    }

    @Override
    public int indexOf(final Object o) {
        return getElementList().indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return getElementList().lastIndexOf(o);
    }


    @Override
    public ListIterator<WebElement> listIterator() {
        return getElementList().listIterator();
    }


    @Override
    public ListIterator<WebElement> listIterator(final int i) {
        return getElementList().listIterator(i);
    }


    @Override
    public List<WebElement> subList(final int i, final int i1) {
        return getElementList().subList(i, i1);
    }

    @Override
    public Spliterator<WebElement> spliterator() {
        return getElementList().spliterator();
    }

    @Override
    public boolean removeIf(final Predicate<? super WebElement> filter) {
        return getElementList().removeIf(filter);
    }

    @Override
    public Stream<WebElement> stream() {
        return getElementList().stream();
    }

    @Override
    public Stream<WebElement> parallelStream() {
        return getElementList().parallelStream();
    }

    @Override
    public void forEach(final Consumer<? super WebElement> action) {
        getElementList().forEach(action);
    }
}
