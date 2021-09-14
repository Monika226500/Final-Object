package com.zhumeng.mall.consumer.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.model.UmsAdmin;
import com.zhumeng.api.service.ITokenService;
import com.zhumeng.api.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 创建人：朱蒙
 * 创建时间：2021/1/24/23:20
 * 描述你的类：拦截器，是在所有类之前运行的类
 * @author Lenovo
 */

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.ITokenService",
            interfaceClass = ITokenService.class,
            timeout = 120000
    )
    private ITokenService tokenService;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IUserService",
            interfaceClass = IUserService.class,
            timeout = 120000
    )
    private IUserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //取得token
        String token=request.getHeader("Authorization");
        //看在controller阶段是否要求必须经过token验证的方法，如果没有就直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //必须需要验证的
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        if(method.isAnnotationPresent(UserLoginToken.class)){
            //这些所有方法，哪些被要求必须经过UserLoginToken修饰
            //取出这个注解
            UserLoginToken userLoginToken=method.getAnnotation(UserLoginToken.class);
            //判断其required是否是true
            if(userLoginToken.required()){
                //如果required为true
                if(token==null){
                    throw new RuntimeException("无token，不能进入");
                }
                //取出token由@进行分割的token码
                //抛出无token的error

                token=token.split("@")[1];
                String userid=tokenService.getUserId(token);
                //通过userid访问ums_admin表，得到该用户记录

                UmsAdmin admin=userService.findUserById(Long.valueOf(userid));
                //userid为String类型，强转为Long类型
                //判断是否有该用户
                if(admin==null){
                    throw new RuntimeException("无此用户！");
                }
                //admin合法要通过token和password进行整体验证
                if(tokenService.checkSign(token,admin.getPassword())){
                    //如果验证成功
                    return true;
                }else {
                    throw new RuntimeException("token验证失败！");
                }
            }
        }


        return true;
        //使用true形成合法的拦截器
    }
}
