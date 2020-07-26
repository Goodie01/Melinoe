package org.goodiemania.melinoe.framework.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.http.HttpClient;
import java.util.HashSet;
import java.util.Set;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;
import org.goodiemania.melinoe.framework.session.logging.MetaLogger;
import org.goodiemania.melinoe.framework.session.logging.writer.LogWriter;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MetaSession {
    private final MetaLogger metaLogger = new MetaLogger();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder().build();

    private Set<ClosableDriver> drivers = new HashSet<>();

    public MetaSession() {
    }

    public void addDriver(final ClosableDriver driver) {
        drivers.add(driver);
    }

    public InternalSessionClassImpl createSessionFor(final ExtensionContext extensionContext) {
        return new InternalSessionClassImpl(this, metaLogger.createClassLogger(extensionContext));
    }

    public File createNewImageFile() {
        return metaLogger.createNewImageFile();
    }

    public void writeLogs() {
        new LogWriter().write(metaLogger);
    }

    public void endSession() {
        Set<ClosableDriver> soonToBeClosedDrivers = drivers;
        drivers = new HashSet<>();
        soonToBeClosedDrivers.forEach(ClosableDriver::close);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
