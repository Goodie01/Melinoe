package org.goodiemania.melinoe.samples.hecate;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.melinoe.ExamplesConfiguration;
import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.framework.api.hecate.HecateListenerDriver;
import org.goodiemania.melinoe.framework.api.hecate.validators.LogResponseContainsListValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
This set of tests require a running Hecate instance
 */
public class createdListenerBasicHecateTest extends MelinoeTest {

    private String hecateHost;
    private int newListenerPort;
    private HecateListenerDriver createdListener;
    private String contextPath;

    @BeforeEach
    void setUp() {
        final int adminPort = Integer.parseInt(ExamplesConfiguration.HECATE_ADMIN_PORT.get());

        hecateHost = ExamplesConfiguration.HECATE_HOST.get();
        newListenerPort = 8180;

        contextPath = "createListenerExample";
        createdListener = getSession().hecate(hecateHost, adminPort)
                .createNewListener(UUID.randomUUID().toString(), "GET", newListenerPort, contextPath);
    }

    @AfterEach
    void tearDown() {
        createdListener.deleteListener();
    }


    @Test
    @DisplayName("Hecate example")
    void test() {
        final String requestIdentifier = UUID.randomUUID().toString();

        getSession().rest(String.format("http://%s:%d/%s", hecateHost, newListenerPort, contextPath))
                .withBody(requestIdentifier)
                .execute();

        createdListener.verify(new LogResponseContainsListValidator(log -> StringUtils.contains(log.getRequest().getBody(), requestIdentifier)));
    }
}
