package nz.geek.goodwin.melinoe.framework.internal.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.api.rest.HttpRequest;
import nz.geek.goodwin.melinoe.framework.api.rest.RestDriver;
import nz.geek.goodwin.melinoe.framework.internal.web.ClosableRegister;

import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Goodie
 */
public class RestDriverImpl implements RestDriver {
    private final ObjectMapper objectMapper;
    private final Logger logger;
    private final HttpClient client;

    //TODO default headers
    public RestDriverImpl(Logger logger, ClosableRegister closableRegister) {
        this.logger = logger;

        ExecutorService executor = Executors.newFixedThreadPool(2);
        closableRegister.add(executor);
        client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).executor(executor).build();
        closableRegister.add(client);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTimeDeserializer dateTimeDeserializer = new LocalDateTimeDeserializer(formatter);
        LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(formatter);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, dateTimeDeserializer);
        javaTimeModule.addSerializer(LocalDateTime.class, dateTimeSerializer);

        objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule);
    }

    @Override
    public HttpRequest request(String method, String url) {
        return new HttpRequestImpl(method, url, client, objectMapper, logger);
    }

    @Override
    public HttpRequest get(String url) {
        return request("GET", url);
    }

    @Override
    public HttpRequest head(String url) {
        return request("HEAD", url);
    }

    @Override
    public HttpRequest options(String url) {
        return request("OPTIONS", url);

    }

    @Override
    public HttpRequest post(String url) {
        return request("POST", url);

    }

    @Override
    public HttpRequest delete(String url) {
        return request("DELETE", url);

    }

    @Override
    public HttpRequest patch(String url) {
        return request("PATCH", url);

    }

    @Override
    public HttpRequest put(String url) {
        return request("PUT", url);

    }

}
