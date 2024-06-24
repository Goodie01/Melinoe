package nz.geek.goodwin.melinoe.framework.api.web;

import nz.geek.goodwin.melinoe.framework.internal.web.CookieImpl;

import java.util.Date;

/**
 * @author Goodie
 */
public interface Cookie {
    static Cookie create() {
        return new CookieImpl();
    }

    String getName();

    Cookie setName(String name);

    String getValue();

    Cookie setValue(String value);

    String getPath();

    Cookie setPath(String path);

    String getDomain();

    Cookie setDomain(String domain);

    Date getExpiry();

    Cookie setExpiry(Date expiry);

    boolean isSecure();

    Cookie setSecure(boolean secure);

    boolean isHttpOnly();

    Cookie setHttpOnly(boolean httpOnly);

    String getSameSite();

    Cookie setSameSite(String sameSite);
}
