package com.bobo.community.controller;

import com.bobo.community.entity.Comment;
import com.bobo.community.entity.DiscussPost;
import com.bobo.community.entity.Event;
import com.bobo.community.event.EventConsumer;
import com.bobo.community.event.EventProducer;
import com.bobo.community.service.CommentService;
import com.bobo.community.service.DiscussPostService;
import com.bobo.community.utils.CommunityConstant;
import com.bobo.community.utils.HostHolder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping(path = "/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private HostHolder hostHolder;
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);


        // 觸發發帖事件
        if(comment.getEntityType() == ENTITY_TYPE_POST)
        {
            event = new Event().setTopic(TOPIC_PUBLISH)
                    .setEntityId(comment.getUserId())
                    .setEntityId(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }

        System.out.println(discussPostId);
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
