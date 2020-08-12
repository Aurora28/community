package com.aurora.community.service;

import com.aurora.community.dao.UserMapper;
import com.aurora.community.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findById(int id) {
        return userMapper.selectById(id);
    }
}