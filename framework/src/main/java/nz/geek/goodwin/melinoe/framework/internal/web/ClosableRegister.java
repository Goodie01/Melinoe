package nz.geek.goodwin.melinoe.framework.internal.web;

import org.openqa.selenium.WebDriver;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Goodie
 */
public class ClosableRegister {
    private final List<AutoCloseable> closeables = new ArrayList<>();

    public void add(WebDriver webDriver) {
        closeables.add(webDriver::quit);
    }
    public void add(AutoCloseable closeable) {
        closeables.add(closeable);
    }

    public List<AutoCloseable> getWebDrivers() {
        return closeables;
    }
}
