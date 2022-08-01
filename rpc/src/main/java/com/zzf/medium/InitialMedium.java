package com.zzf.medium;

import com.zzf.annotation.Remote;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 中介者模式
 * @author zzf
 * @date 2022/7/30 10:09 下午
 */
@Component
public class InitialMedium implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Remote.class)) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String key = bean.getClass().getInterfaces()[0].getName() + "." + method.getName();
                BaseMedia baseMedia = new BaseMedia();
                baseMedia.setBean(bean);
                baseMedia.setMethod(method);
                Media.beanMap.put(key, baseMedia);
            }
        }
        return null;
    }
}
