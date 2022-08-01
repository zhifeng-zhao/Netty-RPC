package com.zzf.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzf
 * @date 2022/7/30 9:40 下午
 */
@Configuration
@ComponentScan("com.zzf")
public class SpringServer {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringServer.class);
    }
}
