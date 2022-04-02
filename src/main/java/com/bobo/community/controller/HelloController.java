package com.bobo.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/sayhello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring";
    }
}
