package com.aurora.community.controller.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aurora.community.entity.LoginTicket;
import com.aurora.community.entity.User;
import com.aurora.community.service.UserService;
import com.aurora.community.util.CookieUtil;
import com.aurora.community.util.HostHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor{

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // get loginTicket from cookie
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            // look up loginTicket
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // check valid 
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // get the current user by ticket
                User user = userService.findById(loginTicket.getUserId());
                // carry user in this request
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        hostHolder.clear();
    }

}