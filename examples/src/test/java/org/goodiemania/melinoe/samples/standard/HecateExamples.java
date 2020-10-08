package org.goodiemania.melinoe.samples.standard;

import org.goodiemania.melinoe.framework.api.MelinoeTest;
import org.goodiemania.melinoe.framework.api.hecate.HecateListenerDriver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HecateExamples extends MelinoeTest {
    @Test
    @DisplayName("Hecate example")
    void test() {
        final HecateListenerDriver bobbyDropTables = getSession().hecate("http://localhost:8443/")
                .createForListener("bobbyDropTables");
    }
}
