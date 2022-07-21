package com.github.soohea.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.soohea.blog.service.AuthService;
import com.github.soohea.blog.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        AuthService authService = new AuthService(userService);
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager, authService)).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("用户没有登录")));
    }


    @Test
    void testLogin() throws Exception {
        //未登录时，/auth接口返回未登录状态
        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("用户没有登录")));

        //使用/auth/login登录
        Map<String, String> usernamePassword = new HashMap<>();
        usernamePassword.put("username", "MyUser");
        usernamePassword.put("password", "MyPassword");

        Mockito.when(userService.getUserByUsername("MyUser")).thenReturn(new com.github.soohea.blog.entity.User(123, "MyUser", bCryptPasswordEncoder.encode("MyPassword")));

        MvcResult response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36")
                        .content(new ObjectMapper().writeValueAsString(usernamePassword)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("登录成功")))
                .andReturn();

        HttpSession session = response.getRequest().getSession();

        //再次检查/auth的返回值，处于登录状态
        mvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse resultResponse = result.getResponse();
                    resultResponse.setDefaultCharacterEncoding("utf-8");
                    Assertions.assertTrue(resultResponse.getContentAsString().contains("MyUser"));
                });


    }
}

