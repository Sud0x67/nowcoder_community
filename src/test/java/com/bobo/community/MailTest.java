package com.bobo.community;

import com.bobo.community.service.DiscussPostService;
import com.bobo.community.utils.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendMail(){
        System.out.println(mailClient);
        System.out.println(discussPostService);
        this.mailClient.sendMail("1789036325@qq.com", "test java", "hhhhhhhhhhhhhhhhhhhhhhhh");
    }
    @Test
    public void sendHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "wx");
        String content = templateEngine.process("/mail/demo.html", context);
        System.out.println(content);
    }
}
