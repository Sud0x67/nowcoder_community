package com.bobo.community.controller;

import com.bobo.community.annotation.LoginRequired;
import com.bobo.community.entity.User;
import com.bobo.community.service.FollowService;
import com.bobo.community.service.LikeService;
import com.bobo.community.service.UserService;
import com.bobo.community.utils.CommunityConstant;
import com.bobo.community.utils.CommunityUtil;
import com.bobo.community.utils.HostHolder;
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

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping(path="/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;

    @LoginRequired
    @RequestMapping(path="/setting", method = RequestMethod.GET)
    public String setUserProfile(){
        return "/site/setting";
    }
    @RequestMapping(path="/profile/{userId}", method = RequestMethod.GET)
    public String getProfile(@PathVariable("userId") int userId, Model model){
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("用户不存在");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }
    @LoginRequired
    @RequestMapping(path="/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        System.out.println(headerImage);
        System.out.println("===============================================================================");
        if(headerImage==null){
            model.addAttribute("error", "请选择正确的文件");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path="/header/{file}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("file") String file,  HttpServletResponse response){
        String suffix = file.substring(file.lastIndexOf("."));
        file = uploadPath + "/" + file;
        response.setContentType("image/" + suffix);
        try(InputStream input = new FileInputStream(file);){
            OutputStream output = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = input.read(buffer)) != -1) {
                output.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }
}
