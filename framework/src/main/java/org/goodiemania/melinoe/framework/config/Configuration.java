package org.goodiemania.melinoe.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;

public enum Configuration {
    BROWSER,
    BROWSER_EXE_LOCATION;

    public String get() {
        String propertyFromSystem = System.getProperty(this.toString());

        if (StringUtils.isNotBlank(propertyFromSystem)) {
            return propertyFromSystem;
        }

        Properties properties = new Properties();
        try (InputStream resourceAsStream = Configuration.class.getResourceAsStream("/default.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException | NullPointerException e) {
            throw new MelinoeException("Unable to load properties file", e);
        }
        return (String) properties.get(this.toString());
    }
}
