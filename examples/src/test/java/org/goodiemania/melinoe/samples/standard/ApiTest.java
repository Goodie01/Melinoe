package org.goodiemania.melinoe.samples.standard;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.rest.validation.RestValidator;
import nz.geek.goodwin.melinoe.framework.internal.MelinoeExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(MelinoeExtension.class)
public class ApiTest {
    private Session session;

    @Test
    @DisplayName("Baseline test")
    public void run() {
        session = Session.create();
        session.rest().get("https://1.1.1.1/dns-query?name=cloudflare.com")
                .header("accept", "application/dns-json")
                .result()
                .verify(List.of(RestValidator.statusCodeEquals(200)));
    }
}
