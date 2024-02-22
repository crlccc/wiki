package com.example.wiki.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截器：spring框架的拦截器，用于登录验证，授权校验，请求日志打印，请求参数校验等。
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //打印请求信息
        log.info("--------------LoginInterceptor 开始------------------");
        log.info("请求地址：{}{}",request.getRequestURL().toString(),request.getMethod());
        log.info("远程地址：{}",request.getRemoteAddr());

        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime",startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception{
        long startTime = (long) request.getAttribute("requestStartTime");
        log.info("-------------------LoginInterceptor 结束 耗时 ：{} ms----------",System.currentTimeMillis()-startTime);
    }
}
