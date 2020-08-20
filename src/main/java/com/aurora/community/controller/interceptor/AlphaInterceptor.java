package com.aurora.community.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AlphaInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AlphaInterceptor.class);
    
    // before controller
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
            logger.info("preHandle: " + handler.toString());
            return true;
    }

    // after controller
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        logger.info("postHandle: " + handler.toString());
    }

    // after TemplateEngine
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("afterCompletion" + handler.toString());
    }

}