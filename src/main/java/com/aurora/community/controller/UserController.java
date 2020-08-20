package com.aurora.community.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aurora.community.annotation.LoginRequired;
import com.aurora.community.entity.User;
import com.aurora.community.service.UserService;
import com.aurora.community.util.CommunityUtil;
import com.aurora.community.util.HostHolder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.domain}")
    private String domain;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "you have still not choosen an image");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        System.out.println(fileName);
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "image format is wrong");
            return "/site/setting";
        }
        fileName = CommunityUtil.generateUUID() + suffix; 
        System.out.println(uploadPath + "/" + fileName);
        // save path
        File dest = new File(uploadPath + "/" + fileName);
        try { 
            // save the file
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("uploade image failure", e.getMessage());
            throw new RuntimeException("upload fail, server does not work", e);
        }

        // update current user header
        // http://localhost:2128/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }
    
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // path in server
        fileName = uploadPath + "/" + fileName;
        // file suffix
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        
        // response a image
        response.setContentType("image/" + suffix);
        try (
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(fileName);  
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("header image read fail", e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(Model model, String password, String newPassword, String rePassword) {
        Map<String, Object> map = userService.setPassword(password, newPassword, rePassword);
        User user = hostHolder.getUser();
        String tmp = CommunityUtil.md5(password + user.getSalt());
        if (!tmp.equals(user.getPassword())) {
            map.put("passwordMsg", "original password is wrong");
        }
        
        if (map == null || map.isEmpty()) {
            userService.updatePassword(user.getId(), newPassword);
            model.addAttribute("msg", "Your password has been changed!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            model.addAttribute("rePasswordMsg", map.get("rePasswordMsg"));
            return "/site/setting";
        }
    }
}