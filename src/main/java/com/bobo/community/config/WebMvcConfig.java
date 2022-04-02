package com.bobo.community.config;
import com.bobo.community.controller.intercepter.LoginRequiredIntercepter;
import com.bobo.community.controller.intercepter.LoginTicketIntercepter;
import com.bobo.community.controller.intercepter.MessageIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private LoginTicketIntercepter loginTicketInterceptor;
    @Autowired
    private LoginRequiredIntercepter loginRequiredIntercepter;
    @Autowired
    private MessageIntercepter messageIntercepter;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/usr/**","/**/*.jpg", "/**/*.jpeg");
        registry.addInterceptor(loginRequiredIntercepter)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/usr/**","/**/*.jpg", "/**/*.jpeg");
        registry.addInterceptor(messageIntercepter)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/usr/**","/**/*.jpg", "/**/*.jpeg");
    }

}
