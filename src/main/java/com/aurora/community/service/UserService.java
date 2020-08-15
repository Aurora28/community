package com.aurora.community.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.aurora.community.dao.LoginTicketMapper;
import com.aurora.community.dao.UserMapper;
import com.aurora.community.entity.LoginTicket;
import com.aurora.community.entity.User;
import com.aurora.community.util.CommunityConstant;
import com.aurora.community.util.CommunityUtil;
import com.aurora.community.util.MailClient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class UserService implements CommunityConstant{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired 
    private TemplateEngine templateEngine;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        
        if (user == null) {
            throw new IllegalAccessError("parameter user cannot be null");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "username cannot be null");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "password cannot be null");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "email cannot be null");
            return map;
        }
        // valify the account
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "this account has already existd");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "this email has already been registed");
            return map;
        }
        
        // register user
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        // activate email
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:2128/communnity/activation/10/code
        String url = domain + contextPath + "/activation" + "/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "activate your email", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAIL;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "username cannot be empty");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "password cannot be empty");
            return map;
        }

        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "this account does not exist");
            return map;
        }
        // check status
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "this account has not been activate");
            return map;
        }
        // check password
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "password does not match");
            return map;
        }

        // generate ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * expiredSeconds));

        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }
}