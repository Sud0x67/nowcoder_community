package com.bobo.community;


import com.bobo.community.dao.UserMapper;
import com.bobo.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void testUserInert(){
        User  user = new User();
        user.setActivationCode("7899");
        user.setUsername("helo");
        userMapper.insertUser(user);
    }
}
