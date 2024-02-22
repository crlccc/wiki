//package com.example.wiki.config;
//
//import com.example.wiki.interceptor.LoginInterceptor;
//import jakarta.annotation.Resource;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Resource
//    LoginInterceptor loginInterceptor;
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
//    }
//
//}
