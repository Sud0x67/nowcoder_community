package com.bobo.community.controller;


import com.bobo.community.entity.DiscussPost;
import com.bobo.community.entity.Page;
import com.bobo.community.entity.User;
import com.bobo.community.service.DiscussPostService;
import com.bobo.community.service.LikeService;
import com.bobo.community.service.MessageService;
import com.bobo.community.service.UserService;
import com.bobo.community.utils.CommunityConstant;
import com.bobo.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private  DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    @RequestMapping(path="/", method = RequestMethod.GET)
    public String root(){
        return "redirect:/index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
//        User me = hostHolder.getUser();
//        int letterUnreadCount = me==null? 0 : messageService.findLetterUnreadCount(me.getId(), null);
        model.addAttribute("discussPosts", discussPosts);
//        model.addAttribute("letterUnreadCount", letterUnreadCount);
        return "/index";
    }
}
