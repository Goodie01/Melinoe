package nz.geek.goodwin.melinoe.framework;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.web.validation.ValidationResult;
import nz.geek.goodwin.melinoe.framework.api.web.WebDriver;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello there");

        Session session = Session.create();
        WebDriver web = session.web();
        web.navigate().to("https://www.nzx.com/instruments/CEN/dividends");
        web.verify(List.of(webDriver -> {
            if(StringUtils.equals(webDriver.getTitle(),"Selene is crawling")) {
                return ValidationResult.passed("Title is: Selene is crawling");
            } else {
                return ValidationResult.passed("Title is not: Selene is crawling");
            }
        }));

        Session.closeAll();
    }
}
