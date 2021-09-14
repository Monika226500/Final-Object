package com.zhumeng.api.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建人：Jason
 * 创建时间：2020/1/9
 * 描述你的类：需要登录才能进行操作的注解
 * @author Lenovo
 */
//注解的作用目标
@Target({ElementType.METHOD,ElementType.TYPE})
//:这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用。
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginToken {
    boolean required()default true;//必须经过登录token验证
}
