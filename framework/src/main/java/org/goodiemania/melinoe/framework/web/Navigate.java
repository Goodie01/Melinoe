package org.goodiemania.melinoe.framework.web;

public interface Navigate {

    void back();

    void forward();

    void to(String url);

    void refresh();
}
