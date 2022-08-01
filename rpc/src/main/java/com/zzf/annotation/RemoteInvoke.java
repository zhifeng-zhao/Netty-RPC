package com.zzf.annotation;

import java.lang.annotation.*;

/**
 * @author zzf
 * @date 2022/7/31 5:31 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoteInvoke {
}
