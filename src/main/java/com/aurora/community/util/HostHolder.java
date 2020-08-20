package com.aurora.community.util;

import com.aurora.community.entity.User;

import org.springframework.stereotype.Component;

// carry user information, substitude session
@Component
public class HostHolder {
    
    private ThreadLocal<User> users = new ThreadLocal<>();
    
    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}