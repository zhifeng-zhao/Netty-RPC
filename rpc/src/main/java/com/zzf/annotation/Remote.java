package com.zzf.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author zzf
 * @date 2022/7/31 5:17 下午
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Remote {

    String value() default "";
}
