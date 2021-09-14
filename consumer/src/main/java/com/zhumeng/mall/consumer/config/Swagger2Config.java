package com.zhumeng.mall.consumer.config;

import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2API文档的配置
 *
 * @author macro
 * @date 2018/4/26
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> predicate = new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                Class<?> declaringClass = input.declaringClass();
                if (declaringClass == BasicErrorController.class){
                    return false;// 排除
                }

                if(declaringClass.isAnnotationPresent(RestController.class)){
                    return true;// 被注解的类
                }

                if(input.isAnnotatedWith(ResponseBody.class)){
                    return true;// 被注解的方法
                }

                return false;
            }
        };
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(predicate)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Mall后台管理系统API文档")
                //大标题
                .version("2.0")
                //版本
                .description("mall后台模块")

                .licenseUrl("http://www.japygo.com")
                .license("哈尔滨易圣通软件开发有限公司")
                .build();
    }

//    @Bean
//    public Docket createRestApi(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.zhumeng.mall.consumer.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("mall后台系统")
//                .description("mall后台模块")
//                .contact("macro")
//                .version("1.0")
//                .build();
//    }

//    private List<ApiKey> securitySchemes() {
//        //设置请求头信息
//        List<ApiKey> result = new ArrayList<>();
//        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
//        result.add(apiKey);
//        return result;
//    }
//
//    private List<SecurityContext> securityContexts() {
//        //设置需要登录认证的路径
//        List<SecurityContext> result = new ArrayList<>();
//        result.add(getContextByPath("/brand/.*"));
//        result.add(getContextByPath("/product/.*"));
//        result.add(getContextByPath("/productCategory/.*"));
//        return result;
//    }
//
//    private SecurityContext getContextByPath(String pathRegex){
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex(pathRegex))
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        List<SecurityReference> result = new ArrayList<>();
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        result.add(new SecurityReference("Authorization", authorizationScopes));
//        return result;
//    }
}
