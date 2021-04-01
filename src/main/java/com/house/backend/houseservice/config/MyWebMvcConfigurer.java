package com.house.backend.houseservice.config;

import com.house.backend.houseservice.common.CorsFilter;
import com.house.backend.houseservice.interceptor.AuthorizationInterceptor;
import com.house.backend.houseservice.interceptor.ReadBodyHttpServletFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 * @date 2018/4/12
 */
@Configuration
@Slf4j
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 跨域拦截器需放在最上面
        registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**").excludePathPatterns("/", "/login");
    }


    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CorsFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ReadBodyHttpServletFilter> requestFilter() {
        FilterRegistrationBean<ReadBodyHttpServletFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ReadBodyHttpServletFilter());
        registration.addUrlPatterns("/*");
        registration.setName("ReadBodyHttpServletFilter");
        registration.setOrder(1);
        return registration;
    }
}

