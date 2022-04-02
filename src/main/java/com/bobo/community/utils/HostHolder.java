package com.bobo.community.utils;

import com.bobo.community.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
