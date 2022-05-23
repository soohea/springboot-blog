package com.github.soohea.blog.controller;

import com.github.soohea.blog.entity.Result;
import com.github.soohea.blog.entity.User;
import com.github.soohea.blog.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;

    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    //判断用户是否登录
    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User loggedInUser = userService.getUserByUsername(authentication == null ? null : authentication.getName());
        if (loggedInUser == null) {
            return new Result("ok", "用户未登录", false);
        } else {
            return new Result("ok", null, true, loggedInUser);
        }

    }

    //登出接口
    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);
        if (loggedInUser == null) {
            return Result.failure("用户未登录");
        } else {
            SecurityContextHolder.clearContext();
            return new Result("ok", "success", false);
        }

    }


    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, Object> userNameAndPassword) {
        String username = (String) userNameAndPassword.get("username");
        String password = (String) userNameAndPassword.get("password");
        if (username == null || password == null) {
            return Result.failure("username/password == null");
        }
        if (username.length() < 1 || username.length() > 15) {
            return Result.failure("invalid username");
        }
        if (password.length() < 1 || password.length() > 15) {
            return Result.failure("invalid password");
        }

        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return Result.failure("user already exists");
        }
        return new Result("ok", "success!", false);

    }


    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, Object> usernameAndPassword) {
        String username = usernameAndPassword.get("username").toString();
        String password = usernameAndPassword.get("password").toString();
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failure("用户不存在");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);

            return new Result("ok", "登录成功", true,
                    userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return Result.failure("密码不正确");

        }
    }


}
