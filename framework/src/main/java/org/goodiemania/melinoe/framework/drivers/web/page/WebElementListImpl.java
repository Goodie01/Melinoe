package org.goodiemania.melinoe.framework.drivers.web.page;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.api.web.By;
import org.goodiemania.melinoe.framework.api.web.ConvertMelinoeBy;
import org.goodiemania.melinoe.framework.api.web.WebElement;
import org.goodiemania.melinoe.framework.session.InternalSession;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebElementListImpl implements List<WebElement> {
    private final InternalSession internalSession;
    private Function<RemoteWebDriver, List<WebElement>> webElementSupplier;

    public WebElementListImpl(final InternalSession internalSession,
                              final Function<RemoteWebDriver, List<org.openqa.selenium.WebElement>> seleniumWebElementListSupplier) {
        this.internalSession = internalSession;
        this.webElementSupplier = remoteWebDriver -> seleniumWebElementListSupplier.apply(getDriver())
                .stream()
                .map(webElement -> (WebElement) new WebElementImpl(internalSession, remoteWebDriver2 -> webElement))
                .collect(Collectors.toUnmodifiableList());
    }

    public WebElementListImpl(final InternalSession internalSession, final By by) {
        this(internalSession, remoteWebDriver -> ConvertMelinoeBy.build(by).findElements(remoteWebDriver));
    }

    private RemoteWebDriver getDriver() {
        if (!internalSession.getRawWebDriver().hasPageBeenChecked()) {
            throw new MelinoeException("Please check page before interacting with it");
        }

        return internalSession.getRawWebDriver().getRemoteWebDriver();
    }

    private List<WebElement> getElementList() {
        return webElementSupplier.apply(getDriver());
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
