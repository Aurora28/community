package com.aurora.community.service;

import java.util.List;

import com.aurora.community.dao.DiscussPostMapper;
import com.aurora.community.entity.DiscussPost;
import com.aurora.community.util.SensitiveFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostsRows(userId);
    }

    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("parameter cannot be empty");
        }
        // encode html tag
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        
        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

}