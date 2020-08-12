package com.aurora.community.controller;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurora.community.entity.DiscussPost;
import com.aurora.community.entity.Page;
import com.aurora.community.entity.User;
import com.aurora.community.service.DiscussPostService;
import com.aurora.community.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @RequestMapping(path="/index", method=RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        // before use this function, Spring MVC will instantialize model and page, insert page into model
        // so we can directly use page in thymeleaf
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPost = new ArrayList<>();
        if (list != null) {
            System.out.println(list.size());
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findById(post.getUserId());
                map.put("user", user);
                discussPost.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPost);
        return "index";
    }
}