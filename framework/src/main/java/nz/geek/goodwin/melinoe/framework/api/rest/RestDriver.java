package nz.geek.goodwin.melinoe.framework.api.rest;

/**
 * @author Goodie
 */
public interface RestDriver {
    HttpRequest request(String method, String url);

    HttpRequest get(String url);

    HttpRequest head(String url);

    HttpRequest options(String url);

    HttpRequest post(String url);

    HttpRequest delete(String url);

    HttpRequest patch(String url);

    HttpRequest put(String url);
}
