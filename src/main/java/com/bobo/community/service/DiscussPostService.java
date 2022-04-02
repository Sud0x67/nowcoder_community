package com.bobo.community.service;

import com.bobo.community.dao.DiscussPostMapper;
import com.bobo.community.entity.DiscussPost;
import com.bobo.community.utils.SensitiveFilter;
import org.apache.el.stream.Stream;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    SensitiveFilter sensitiveFilter;
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return this.discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public  int addDiscussPost(DiscussPost post){
        if(post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词

        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }
    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public void updateCommentCount(int entityId, int count) {
        discussPostMapper.updateCommentCount(entityId, count);
    }
}
