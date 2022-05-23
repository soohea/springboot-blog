package com.github.soohea.blog;

import com.github.soohea.blog.entity.User;
import com.github.soohea.blog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestSQL {
    @Autowired
    private UserService userService;

    @Test
    public void test1(){

        userService.save("aaa","aaa");

    }
}
