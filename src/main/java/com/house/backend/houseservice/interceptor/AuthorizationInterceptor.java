package com.house.backend.houseservice.interceptor;


import com.house.backend.houseservice.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义拦截器，对请求进行身份验证
 *
 * @author zhengshuqin
 * @date 2019/12/17
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    public boolean isApiWhiteName(String requestUri) {
        String apiWhiteName = SpringContextUtil.getProperty("app.apiWhiteName");
        if (null == apiWhiteName) {
            return false;
        }
        logger.info("{},{}", requestUri, apiWhiteName);
        String[] whileApis = apiWhiteName.split(",");
        for (String s : whileApis) {
            if (requestUri.contains(s)) {
                return true;
            }
        }
        // path uri 处理
        for (String wapi : whileApis) {
            if (wapi.contains("{") && wapi.contains("}")) {
                if (wapi.split("/").length == requestUri.split("/").length) {
                    String reg = wapi.replaceAll("\\{.*}", ".*{1,}");
                    System.err.println(reg);
                    Pattern r = Pattern.compile(reg);
                    Matcher m = r.matcher(requestUri);
                    if (m.find()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        super.postHandle(request, response, handler, modelAndView);
    }


}
