package nz.geek.goodwin.melinoe.framework.internal.web;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.UUID;

/**
 * @author Goodie
 */
public class PageCheckStatus {
    private static final String RELOAD_FLAG_VALUE = "reloadFlag";
    private final LocalStorage localStorage;
    private String reloadFlag = RELOAD_FLAG_VALUE;

    public PageCheckStatus(final RemoteWebDriver remoteWebDriver) {
        this.localStorage = new LocalStorage(remoteWebDriver);
    }

    public boolean check() {
        return StringUtils.equals(localStorage.get(reloadFlag), RELOAD_FLAG_VALUE);
    }

    public void set() {
        reloadFlag = UUID.randomUUID().toString();
        localStorage.put(reloadFlag, RELOAD_FLAG_VALUE);
    }
}
