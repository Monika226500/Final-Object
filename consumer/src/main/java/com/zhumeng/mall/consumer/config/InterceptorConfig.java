package com.zhumeng.mall.consumer.config;

import com.zhumeng.mall.consumer.aop.SystemLogAspect;
import com.zhumeng.mall.consumer.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 创建人：朱蒙
 * 创建时间：2021/1/24/23:16
 * 描述你的类：
 * @author Lenovo
 */
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired//自动装配
    private AuthInterceptor authInterceptor;

    @Autowired
    private SystemLogAspect systemLogAspect;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        registry.addInterceptor(systemLogAspect).addPathPatterns("/**");
        //拦截器
    }
}
