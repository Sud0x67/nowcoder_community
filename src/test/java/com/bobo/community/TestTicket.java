package com.bobo.community;

import com.bobo.community.dao.LoginTicketMapper;
import com.bobo.community.entity.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TestTicket {
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void test(){
        LoginTicket lt = new LoginTicket();
        lt.setTicket("abc");
        lt.setUserId(1212223);
        lt.setStatus(0);
        lt.setExpired(new Date(System.currentTimeMillis() + 1000000));
        loginTicketMapper.insertLoginTicket(lt);


    }
    @Test
    public void testSelect(){
        var ls = loginTicketMapper.selectByTicket("abc");
        System.out.println(ls);
    }
    @Test
    public void updateStatus(){
        System.out.println(loginTicketMapper.updateStatus("abc", 1));
    }
}
