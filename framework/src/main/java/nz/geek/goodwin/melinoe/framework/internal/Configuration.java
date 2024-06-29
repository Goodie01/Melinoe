package nz.geek.goodwin.melinoe.framework.internal;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Goodie
 */
public enum Configuration {
    BROWSER,
    BROWSER_EXE_LOCATION,
    RETRY_COUNT("5"),
    RETRY_SLEEP_TIME_MS("100"),
    ;

    private static Properties PROPERTIES;

    private final String defaultValue;

    Configuration() {
        this.defaultValue = null;
    }

    Configuration(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    private Object valueOf() {
        String propertyName = this.toString();
        String propertyFromSystem = System.getenv(propertyName);

        if (StringUtils.isNotBlank(propertyFromSystem)) {
            return propertyFromSystem;
        }

        if(PROPERTIES == null) {
            PROPERTIES = new Properties();
            String propertyFile = System.getProperty("props", "default.properties");

            try (FileReader propertiesFileReader = new FileReader(propertyFile)) {
                PROPERTIES.load(propertiesFileReader);
            } catch (IOException | NullPointerException e) {
                throw new IllegalStateException("Unable to load properties file", e);
            }
        }

        return PROPERTIES.getProperty(propertyName, defaultValue);
    }
    public String val() {
        return valueOf().toString();
    }
    public int intVal() {
        return Integer.parseInt(valueOf().toString());
    }
}
