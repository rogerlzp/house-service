package com.house.backend.houseservice.common;


import com.alibaba.fastjson.JSON;
import com.house.backend.houseservice.config.WebServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Jack.HOu on 2018/4/12.
 */

public class CorsFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        long startTime = System.currentTimeMillis();
        final String requestUri = request.getRequestURI();

        WebServletContext.initWebServletContext(request, response);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "sysType,token,netNo,language,Authorization,DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Upgrade");
        // 如果是OPTIONS则结束请求
        if (!HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            chain.doFilter(req, res);
            long times = System.currentTimeMillis() - startTime;
            if (times > 3000) {
                logger.error("requestUri:{} 耗时超过3s，性能太差，需要优化.......................", requestUri);
            }
            logger.info("requestUri:{} 耗时 : {} ms", requestUri, times);

        } else {
            AjaxResult result = AjaxResult.ok();
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.OK.value());
            String userJson = JSON.toJSONString(result);
            OutputStream out = response.getOutputStream();
            out.write(userJson.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }

    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}

