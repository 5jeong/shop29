package com.toy2.shop29.cart.config;

import com.toy2.shop29.cart.interceptor.CartSessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CartSessionInterceptor cartSessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartSessionInterceptor)
                .addPathPatterns("/cart/**"); // '/cart'로 시작하는 URL에만 인터셉터 적용
    }
}