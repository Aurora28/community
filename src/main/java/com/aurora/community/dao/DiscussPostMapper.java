package com.aurora.community.dao;

import java.util.List;

import com.aurora.community.entity.DiscussPost;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DiscussPostMapper {
    
    // extention for future myPost, if == 0 return all post
    // future will be divide page
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit); 

    // nickName @Param
    // if dynamically use parameters(<if>) && only has one parameter -> must use @Param
    int selectDiscussPostsRows(@Param("userId") int userId);

}