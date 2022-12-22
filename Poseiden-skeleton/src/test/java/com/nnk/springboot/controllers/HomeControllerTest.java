package com.nnk.springboot.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HomeControllerTest {
  

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void homeTest_whenNoAuthentication_ThenReturnHome() throws Exception {
        mockMvc.perform(get("/").with(authentication(null))).andExpect(status().isOk()).andExpect(view().name("home"));
    }

    @Test
    @WithMockUser(authorities = { "USER" })
    public void homeTest_whenUserRoleUSer_ThenRedirectUserHome() throws Exception {

        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/home"));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    public void homeTest_whenUserRoleAdmin_ThenRedirectUserHome() throws Exception {

        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/home"));
    }

    @Test

    public void homeTest_whenUserRoleNotExist_thenForwardToErrorPage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(authorities = { "USER", "ADMIN" })
    public void userHomeTest() throws Exception {
        mockMvc.perform(get("/user/home")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/bidList/list")).andDo(print());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    public void adminHomeTest_whenRoleAdmin_thenRedirectAdminHome() throws Exception {
        mockMvc.perform(get("/admin/home")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/bidList/list")).andDo(print());
    }

    @Test
    @WithMockUser(authorities = { "USER" })
    public void adminHomeTest_WhenRoleUser_thenForwardToErrorPage() throws Exception {
        mockMvc.perform(get("/admin/home")).andExpect(status().is4xxClientError()).andExpect(forwardedUrl("/app/error")).andDo(print());
    }
}
