package org.goodiemania.melinoe.framework.api.web;

public interface Navigate {

    void back();

    void forward();

    void to(String url);

    void refresh();
}
