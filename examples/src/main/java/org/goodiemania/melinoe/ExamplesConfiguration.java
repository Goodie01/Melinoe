package org.goodiemania.melinoe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.goodiemania.melinoe.framework.api.exceptions.MelinoeException;
import org.goodiemania.melinoe.framework.config.Configuration;

public enum ExamplesConfiguration {
    HECATE_HOST,
    HECATE_ADMIN_PORT;

    public String get() {
        Properties properties = new Properties();
        try (InputStream resourceAsStream = Configuration.class.getResourceAsStream("/default.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException | NullPointerException e) {
            throw new MelinoeException("Unable to load properties file", e);
        }
        return (String) properties.get(this.toString());
    }
}
