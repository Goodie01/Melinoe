package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.web.Cookie;

import java.util.Date;
import java.util.Objects;

/**
 * @author Goodie
 */
public final class CookieImpl implements Cookie {
    private String name;
    private String value;
    private String path;
    private String domain;
    private Date expiry;
    private boolean isSecure;
    private boolean isHttpOnly;
    private String sameSite;

    public CookieImpl() {
    }

    public CookieImpl(org.openqa.selenium.Cookie cookie) {
        this.name = cookie.getName();
        this.value = cookie.getValue();
        this.path = cookie.getPath();
        this.domain = cookie.getDomain();
        this.expiry = cookie.getExpiry();
        this.isSecure = cookie.isSecure();
        this.isHttpOnly = cookie.isHttpOnly();
        this.sameSite = cookie.getSameSite();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Cookie setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Cookie setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Cookie setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public Cookie setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public Date getExpiry() {
        return expiry;
    }

    @Override
    public Cookie setExpiry(Date expiry) {
        this.expiry = expiry;
        return this;
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public Cookie setSecure(boolean secure) {
        isSecure = secure;
        return this;
    }

    @Override
    public boolean isHttpOnly() {
        return isHttpOnly;
    }

    @Override
    public Cookie setHttpOnly(boolean httpOnly) {
        isHttpOnly = httpOnly;
        return this;
    }

    @Override
    public String getSameSite() {
        return sameSite;
    }

    @Override
    public Cookie setSameSite(String sameSite) {
        this.sameSite = sameSite;
        return this;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof Cookie otherCookie) {
            return name.equals(otherCookie.getName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
