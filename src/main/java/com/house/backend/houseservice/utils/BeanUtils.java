package com.house.backend.houseservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Sherry.Wang
 * @Title:BeanUtils
 * @Description:bean帮助类
 * @date 2017/1/12
 */
public class BeanUtils {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    private BeanUtils() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 对象赋值，忽略掉空的值
     *
     * @param source
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 对象赋值，忽略掉空的值和id
     *
     * @param source
     * @param target
     */
    public static void copyPropertiesIgnoreNullAndId(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyAndIdNames(source));
    }

    /**
     * 对象赋值，不忽略掉空的值
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static String[] getNullPropertyAndIdNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
            if ("id".equals(pd.getName())) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 对象赋值，忽略
     *
     * @param source
     * @param target
     */
    public static void copyPropertiesIgnore(Object source, Object target, List<String> ignoreProperties) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(ignoreProperties));
    }

    /**
     * 对象赋值 映射
     *
     * @param source        源
     * @param target        目标
     * @param propertiesMap 映射关系 map<目标字段,原字段>
     * @return modifyProperties target修改的字段
     */
    public static List<String> copyProperties(Object source, Object target, Map<String, String> propertiesMap) {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        List<String> modifyProperties = new ArrayList<>();

        if (null == propertiesMap || propertiesMap.isEmpty()) {
            return modifyProperties;
        }

        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (!propertiesMap.containsKey(targetPd.getName())) {
                continue;
            }
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                String sourceName = propertiesMap.get(targetPd.getName());
                PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(), sourceName);
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                            modifyProperties.add(targetPd.getName());
                        } catch (Throwable ex) {
                            throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
        return modifyProperties;
    }

}
