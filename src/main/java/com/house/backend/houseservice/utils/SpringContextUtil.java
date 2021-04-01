package com.house.backend.houseservice.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 描述:获取spring上下文
 *
 * @author zhengshuqin
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * spring上下文
     */
    private static ApplicationContext applicationContext;


    private static DefaultListableBeanFactory beanFactory;


    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        SpringContextUtil.beanFactory = beanFactory;
    }

    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取对象
     *
     * @param var1
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> var1) {
        return applicationContext.getBean(var1);
    }

    /**
     * 直接回去配置文件中的参数
     *
     * @param property
     * @return
     */
    public static String getProperty(String property) {
        Environment environment = SpringContextUtil.getBean(Environment.class);
        return environment.getProperty(property);
    }
}
