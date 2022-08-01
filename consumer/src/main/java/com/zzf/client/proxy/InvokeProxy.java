package com.zzf.client.proxy;

import com.zzf.client.core.TcpClient;
import com.zzf.client.parm.ClientRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzf
 * @date 2022/7/31 6:26 下午
 */
@Component
public class InvokeProxy implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 获取所有属性
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            final Map<Method, Class> methodClassMap = new HashMap<>();
            putMethodClass(methodClassMap, field);
            // 动态代理
            Enhancer enhancer = new Enhancer();
            enhancer.setInterfaces(new Class[]{field.getType()});
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object instance, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    // 采用netty客户端去调用服务器
                    ClientRequest request = new ClientRequest();
                    request.setContent(objects[0]);
                    request.setCommand(methodClassMap.get(method).getName() + "." + method.getName());
                    return TcpClient.send(request);
                }
            });
            try {
                // 对类进行初始化
                field.set(bean, enhancer.create());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    /**
     * 对属性的所有方法和属性接口类型放入map中
     * @param methodClassMap
     * @param field
     */
    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for (Method method : methods) {
            methodClassMap.put(method, field.getType());
        }
    }
}
