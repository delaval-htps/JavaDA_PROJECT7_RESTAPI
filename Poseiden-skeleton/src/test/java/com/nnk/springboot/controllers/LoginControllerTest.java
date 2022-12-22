package com.nnk.springboot.controllers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(get("/app/login")).andExpect(status().isOk()).andExpect(view().name("login"));
    }

    @Test
    public void getAllUSersArticlesTest() throws Exception {
        mockMvc.perform(get("/app/secure/article-details")).andExpect(status().isOk()).andExpect(view().name("user/list"));
    }

    @Test
    public void errorTest() throws Exception {
        mockMvc.perform(get("/app/error")).andExpect(status().isOk()).andExpect(view().name("403"));
    }
}
