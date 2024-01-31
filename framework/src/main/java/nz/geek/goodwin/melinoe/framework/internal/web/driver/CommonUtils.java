package nz.geek.goodwin.melinoe.framework.internal.web.driver;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public class CommonUtils {
    private CommonUtils() {

    }
    public static String waitForDriverExtraction(final String driverLocation) {
        Optional<String> newDriverLocation;
        while ((newDriverLocation = attemptToExtractDriver(driverLocation)).isEmpty()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //suggestion from sonar
                Thread.currentThread().interrupt();
            }
        }
        return newDriverLocation.get();
    }

    private static Optional<String> attemptToExtractDriver(final String driverLocation) {
        File targetFile = new File("target/" + driverLocation);

        if (!targetFile.exists()) {
            InputStream resourceAsStream = CommonUtils.class.getClassLoader().getResourceAsStream(driverLocation);
            Objects.requireNonNull(resourceAsStream);
            try {
                FileUtils.copyInputStreamToFile(resourceAsStream, targetFile);
            } catch (IOException e) {
                return Optional.empty();
            }
        }

        return Optional.of(targetFile.getAbsolutePath());
    }
}
