package com.toy2.shop29.users;

import com.toy2.shop29.users.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/user/signup",
                        "/user/findId",
                        "/user/findPassword",
                        "/user/checkUserId",
                        "/user/checkUserEmail",
                        "/user/checkUserPhoneNumber",
                        "/product/**",
                        "/cart/**" ,
                        "/faq/**",
                        "/email/**",
                        "/login",
                        "/logout",
                        "/board/**" ,
                        "/css/**",
                        "/error",
                        "/mainPage/**",
                        "/images/**",
                        "/attachment/**"
                        );
    }
}
