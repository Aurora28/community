package com.aurora.community.controller;

import java.util.Date;

import com.aurora.community.entity.DiscussPost;
import com.aurora.community.entity.User;
import com.aurora.community.service.DiscussPostService;
import com.aurora.community.service.UserService;
import com.aurora.community.util.CommunityUtil;
import com.aurora.community.util.HostHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "You have still not logged in.");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        // later will handle error condition
        return CommunityUtil.getJSONString(0, "post sucessfully");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        System.out.println(post.getContent());
        User user = userService.findById(post.getUserId());
        model.addAttribute("user", user);
        System.out.println("user: "+user.getUsername());
        return "site/discuss-detail";
    }
}