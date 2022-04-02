package com.bobo.community;

import com.bobo.community.dao.DiscussPostMapper;
import com.bobo.community.entity.DiscussPost;
import com.bobo.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Stream;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostServiceTest {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private DiscussPostService discussPostService;
    @Test
    public void testDiscussPostMappper(){
        List<DiscussPost>  posts = discussPostMapper.selectDiscussPosts(0, 0,10);
        for(DiscussPost post : posts){
           System.out.println(post);
        }
        posts.stream().forEach(System.out::println);
        Stream.iterate(0,i->i+1).limit(80).map((i)->"=").forEach(System.out::print);
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
        System.out.println("===========================================================");
    }
    @Test
    public void testDiscussPostService(){
        List<DiscussPost>  posts = discussPostService.findDiscussPosts(0, 0,10);
        for(DiscussPost post : posts){
            System.out.println(post);
        }
        System.out.println(discussPostService.findDiscussPostRows(0));
        System.out.println("===========================================================");
    }
    @Test
    public void testSelect(){
        System.out.println(discussPostMapper.selectDiscussPostById(109));
    }

}
