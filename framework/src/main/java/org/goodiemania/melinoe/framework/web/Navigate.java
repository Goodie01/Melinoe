package org.goodiemania.melinoe.framework.web;
import java.net.URL;

public interface Navigate {
    void back();
    void forward();
    void to(String url);
    void refresh();
}
