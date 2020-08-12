package com.aurora.community.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.aurora.community.dao.AlphaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("constructor method");
    }

    @PostConstruct
    public void init() {
        System.out.println("initialize AlphaService");
    }

    @PreDestroy
    public void destory() {
        System.out.println("destory AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }
}