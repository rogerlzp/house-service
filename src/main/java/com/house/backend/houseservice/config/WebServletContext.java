package com.house.backend.houseservice.config;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhengshuqin
 */
@Setter
@Getter
public class WebServletContext {
    public static final String USERINFO ="USER_INFO";
    public static final String BATCH_REQUEST = "BATCH_REQUEST";

    private static ThreadLocal<WebServletContext> localContext = new ThreadLocal<WebServletContext>();
    private HttpServletRequest request;
    private HttpServletResponse response;

    public WebServletContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static void initWebServletContext(HttpServletRequest request, HttpServletResponse response) {
        WebServletContext webContext = new WebServletContext(request, response);
        localContext.set(webContext);
    }

    public static void initWebServletContext(WebServletContext webContext) {
        localContext.set(webContext);
    }

    public static WebServletContext getWebServletContext() {
        return (WebServletContext) localContext.get();
    }

    public static void clear() {
        localContext.remove();
    }

}
