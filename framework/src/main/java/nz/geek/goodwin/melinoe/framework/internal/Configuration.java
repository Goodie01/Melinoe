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
    RETRY_COUNT,
    RETRY_SLEEP_TIME_MS,
    ;

    private Object valueOf() {
        String propertyName = this.toString();
        String propertyFromSystem = System.getenv(propertyName);

        if (StringUtils.isNotBlank(propertyFromSystem)) {
            return propertyFromSystem;
        }

        Properties properties = new Properties();
        String propertyFile = System.getProperty("props", "default.properties");

        try (FileReader propertiesFileReader = new FileReader(propertyFile)) {
            properties.load(propertiesFileReader);
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException("Unable to load properties file", e);
        }
        return properties.get(propertyName);
    }
    public String val() {
        return valueOf().toString();
    }
    public int intVal() {
        return Integer.parseInt(valueOf().toString());
    }
}
