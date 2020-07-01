package org.goodiemania.melinoe.framework.web;
import java.util.List;
import org.goodiemania.melinoe.framework.web.validators.WebValidator;

public interface WebDriver {
    Navigate navigate();

    //TODO you shouldn't just be able to call this, it should be accessable by pages only...
    void checkPage(List<WebValidator> validators);

    String getTitle();
}
