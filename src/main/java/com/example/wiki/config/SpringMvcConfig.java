package com.example.wiki.config;

import com.example.wiki.interceptor.ActionInterceptor;
import com.example.wiki.interceptor.LoginInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    @Resource
    ActionInterceptor actionInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/redis/**",
                        "/user/login",
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/find-content/**",
                        "/doc/vote/**",
                        "/ebook-snapshot/**",
                        "/ebook/upload/avatar",
                        "/file/**"
                );
        registry.addInterceptor(actionInterceptor)
                .addPathPatterns(
                        "/*/save",
                        "/*/delete/**",
                        "/*/reset-password");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("file:/www/wiki/dist/image");
    }

}
