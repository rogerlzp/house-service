package com.house.backend.houseservice.interceptor;

import com.house.backend.houseservice.utils.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ReadBodyHttpServletFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if("POST".equals(httpServletRequest.getMethod())){
            if (StringUtils.isNotEmpty(httpServletRequest.getContentType()) && httpServletRequest.getContentType().contains("multipart/")) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                RepeatedlyReadRequestWrapper requestWrapper = new RepeatedlyReadRequestWrapper(httpServletRequest);
                filterChain.doFilter(requestWrapper, httpServletResponse);
            }
        }else{
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
