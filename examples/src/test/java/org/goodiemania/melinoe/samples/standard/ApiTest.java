package org.goodiemania.melinoe.samples.standard;

import com.fasterxml.jackson.annotation.JsonProperty;
import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.rest.validation.RestValidator;
import nz.geek.goodwin.melinoe.framework.internal.MelinoeExtension;
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
                .verifyAsString(List.of(RestValidator.statusCodeEquals(200)));

        session.rest().get("https://1.1.1.1/dns-query?name=cloudflare.com")
                .header("accept", "application/dns-json")
                .verify(Root.class, List.of(RestValidator.statusCodeEquals(200)));

    }

    public static class Answer {
        public String name;
        public int type;
        @JsonProperty("TTL")
        public int tTL;
        public String data;
    }

    public static class Question {
        public String name;
        public int type;
    }

    public static class Root {
        @JsonProperty("Status")
        public int status;
        @JsonProperty("TC")
        public boolean tC;
        @JsonProperty("RD")
        public boolean rD;
        @JsonProperty("RA")
        public boolean rA;
        @JsonProperty("AD")
        public boolean aD;
        @JsonProperty("CD")
        public boolean cD;
        @JsonProperty("Question")
        public List<Question> question;
        @JsonProperty("Answer")
        public List<Answer> answer;
    }

}
