package nz.geek.goodwin.melinoe.framework.api.web;

/**
 * @author Goodie
 */
public interface Navigate {
    void back();

    void forward();

    void to(String url);

    void refresh();

    String getUrl();
}

