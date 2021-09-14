package com.zhumeng.mall.consumer.aop;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zhumeng.api.anno.SystemLog;
import com.zhumeng.api.common.IpInfoUtil;
import com.zhumeng.api.common.ThreadPoolUtil;
import com.zhumeng.api.model.UMSLog;
import com.zhumeng.api.model.UmsAdmin;
import com.zhumeng.api.service.ITokenService;
import com.zhumeng.api.service.IUMSLogService;
import com.zhumeng.api.service.IUserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/17/11:32
 * 描述你的类：
 * @author Lenovo
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect implements HandlerInterceptor {
    //用线程池保持日志
    private static final ThreadLocal<Date> local=new NamedThreadLocal<>("log save");

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.ITokenService",
            interfaceClass = ITokenService.class,
            timeout = 120000
    )
    private ITokenService tokenService;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IUMSLogService",
            interfaceClass = IUMSLogService.class,
            timeout = 120000
    )
    private IUMSLogService umsLogService;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IUserService",
            interfaceClass = IUserService.class,
            timeout = 120000
    )
    private IUserService userService;
    @Pointcut("@annotation(com.zhumeng.api.anno.SystemLog)")
    public void controllerAspect(){
        System.out.println("ok");
    }
    @Before("controllerAspect()")
    public void before(JoinPoint point){
        local.set(new Date());
    }

    @After("controllerAspect()")
    public void after(JoinPoint joinPoint){
        //第一步取出前端送来的request对象
        try {
            ServletRequestAttributes servletRequestAttribute=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request=servletRequestAttribute.getRequest();
            //第二步取出核心调用Controller层的一些参数
            Map<String,Object> map=getControllerMethodInfo(joinPoint);
            //取出一些request消息
            String token=request.getHeader("Authorization");
            //通过token取出登录后的userid
            if(token==null){
                throw new RuntimeException("你还没有登录!");
            }
            token=token.split("@")[1];
            String userid=tokenService.getUserId(token);
            //通过userid取出用户
            UmsAdmin admin=userService.findUserById(Long.parseLong(userid));
            if(admin==null){
                throw new RuntimeException("没有该用户!");
            }
            //通过token和密码验证是否是合法用户
            if(!tokenService.checkSign(token, admin.getPassword())){
               throw new RuntimeException("该用户是非法用户!");
            }
            //模拟多个用户操作日志
            final Random random=new Random();
            int userIdl=random.nextInt(100);
            UMSLog log=new UMSLog();
            log.setUserid(userIdl);

            //日志标题
            log.setName(map.get("desc").toString());
            //日志类型
            log.setLogType(new Integer(map.get("type").toString()));
            //日志请求url
            log.setRequestUrl(request.getRequestURI());
            //请求方式
            log.setRequestType(request.getMethod());
            Map<String, String[]> requestParams = request.getParameterMap();
            //请求参数
            // log.setRequestParam(ObjectUtil.mapToString(requestParams));
            String objectStr2 = (JSON.toJSONString(requestParams)).replace(",","&&");//必须这样处理，否则 flink无法区做字段值的区分，正常字段值用,分割
            System.out.println(objectStr2);
            log.setRequestParam(objectStr2.replace("\"","\'"));//防止json数据分不清楚

            //其他属性
            log.setIp(IpInfoUtil.getIpAddr(request));

            log.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            log.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            //计算耗时时间
            Long beginTime=local.get().getTime();
            Long endTime=System.currentTimeMillis();
            log.setCostTime(new Long(endTime-beginTime).intValue());
            //耗费时间

            ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(umsLogService,log));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {
        Map<String,Object> map=new HashMap<>(16);
        //获取目标类名
        String targetName=joinPoint.getTarget().getClass().getName();
        System.out.println("------->"+targetName);
        //调用方法名
        String methodName=joinPoint.getSignature().getName();
        System.out.println("------->"+methodName);
        //获取相关参数
        Object[] arguments= joinPoint.getArgs();
        //利用反射生成目标类
        Class<?> c=Class.forName(targetName);
        Method[] ms=c.getMethods();
        //找到那个核心方法，判断是否有SystemLog注解进行修饰
        for(Method m :ms){
            if(!m.getName().equals(methodName)){
                continue;
            }
            //再判断方法参数个数是否一致
            Class[] cs=m.getParameterTypes();
            if(cs.length!=arguments.length){
                continue;
            }
            //找到了哪个核心方法，判断是否在该方法前有SystemLog注解
            map.put("desc",m.getAnnotation(SystemLog.class).description());
            map.put("type",m.getAnnotation(SystemLog.class).type().ordinal());//必须取出枚举类型序号
        }
        return map;
    }
}
