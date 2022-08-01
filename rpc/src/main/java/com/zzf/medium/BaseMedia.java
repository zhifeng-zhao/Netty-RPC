package com.zzf.medium;

import java.lang.reflect.Method;

/**
 * @author zzf
 * @date 2022/7/31 12:05 下午
 */
public class BaseMedia {

    private Object bean;

    private Method method;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
