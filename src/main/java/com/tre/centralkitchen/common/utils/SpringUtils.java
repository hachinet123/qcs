package com.tre.centralkitchen.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;

/**
 * spring tool class
 *
 * @author Lion Li
 */
@Component
public final class SpringUtils extends SpringUtil {

    /**
     * Returns true if the BeanFactory contains a bean definition matching the given name
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    /**
     * Determines whether the bean definition registered with the given name is a singleton or a prototype.
     * If no bean definition corresponding to the given name is found, an exception (NoSuchBeanDefinitionException) will be thrown
     *
     * @param name
     * @return boolean
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isSingleton(name);
    }

    /**
     * @param name
     * @return Class Type of registered object
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getType(name);
    }

    /**
     * If the given bean name has aliases in the bean definition, return those aliases
     *
     * @param name
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getAliases(name);
    }

    /**
     * Get the aop proxy object
     *
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }

}
