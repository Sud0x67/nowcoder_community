package com.bobo.community.controller;


import com.bobo.community.entity.*;
import com.bobo.community.event.EventProducer;
import com.bobo.community.service.CommentService;
import com.bobo.community.service.DiscussPostService;
import com.bobo.community.service.LikeService;
import com.bobo.community.service.UserService;
import com.bobo.community.utils.CommunityConstant;
import com.bobo.community.utils.CommunityUtil;
import com.bobo.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        Event event = new Event().setTopic(TOPIC_PUBLISH)
                .setEntityId(user.getId())
                .setEntityId(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{postId}", method = RequestMethod.GET)
    public String postDetail(@PathVariable("postId") int postId, Model model, Page page) {
        DiscussPost discussPost = discussPostService.findDiscussPostById(postId);
        model.addAttribute("post", discussPost);
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId());
        model.addAttribute("likeCount", likeCount);
        int likeStatus = hostHolder.getUser()==null? 0: likeService.findEntityLikeStatus(
                hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPost.getId());


        page.setLimit(5);
        page.setPath("/discuss/detail/" + postId);
        page.setRows(discussPost.getCommentCount());

        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, postId, page.getOffset(), page.getLimit());
        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                likeStatus = hostHolder.getUser()==null? 0: likeService.findEntityLikeStatus(
                        hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());

                commentVo.put("likeStatus", likeStatus);
                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        likeStatus = hostHolder.getUser()==null? 0: likeService.findEntityLikeStatus(
                                hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());

                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }
}
