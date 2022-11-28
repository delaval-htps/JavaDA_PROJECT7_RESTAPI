package com.nnk.springboot.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.security.AuthProvider;
import com.nnk.springboot.services.UserService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private User user1, user2;
    private List<User> users;

    @Before
    public void initialize() {

        user1 = new User("user1", "user1@email.com", "user1Fullname", "USER", AuthProvider.GITHUB, 123);
        user2 = new User("user2", "user2@email.com", "user2Fullname", "ADMIN", AuthProvider.GITHUB, 312);
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void homeTest() throws Exception {
        //given

        //when
        when(userService.findAll()).thenReturn(users);
        
        //then
        mockMvc.perform(get("/user/list"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/list"))
        .andExpect(model().attribute("users",users));
    }

    @Test
    public void addUserTest() throws Exception {
        // given & when

        // then
        mockMvc.perform(get("/user/add")).andExpect(status().isOk()).andExpect(view().name("user/add"));
    }

    @Test
    public void validateTest_whenUserNotValide_thenRedirectAddUser() throws Exception {
        // when & then
        mockMvc.perform(post("/user/validate").param("username", "").param("fullname", "").param("email", "").param("password", "").with(csrf())).andExpect(view().name("user/add"));

        verify(userService, never()).saveUser(Mockito.any(User.class));
    }

    @Test
    public void validateTest_whenUserValidAndNotExisting_thenSaveNewUser() throws Exception {
        // when
            when(userService.findByUsername(anyString())).thenReturn(null);
            user1.setPassword("Jadmin4all&lp4e");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // then
        mockMvc.perform(post("/user/validate")
            .param("username", user1.getUsername())
            .param("fullname", user1.getFullname())
            .param("email", user1.getEmail())
            .param("role", user1.getRole())
            .param("password", user1.getPassword())
            .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

                ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userService, times(1)).saveUser(userCaptor.capture());
        assertEquals(userCaptor.getValue().getFullname(), user1.getFullname());
        assertEquals(userCaptor.getValue().getEmail(), user1.getEmail());
        assertEquals(userCaptor.getValue().getRole(), user1.getRole());
        assertTrue(encoder.matches(user1.getPassword(),userCaptor.getValue().getPassword()));
    }

    @Test
    public void validateTest_whenUserValidAndExisting_thenRedirectAddUser() throws Exception {
        // when
            when(userService.findByUsername(anyString())).thenReturn(user1);
            user1.setPassword("Jadmin4all&lp4e");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // then
        mockMvc.perform(post("/user/validate")
            .param("username", user1.getUsername())
            .param("fullname", user1.getFullname())
            .param("email", user1.getEmail())
            .param("role", user1.getRole())
            .param("password", user1.getPassword())
            .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

                ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userService, times(1)).saveUser(userCaptor.capture());
        assertEquals(userCaptor.getValue().getFullname(), user1.getFullname());
        assertEquals(userCaptor.getValue().getEmail(), user1.getEmail());
        assertEquals(userCaptor.getValue().getRole(), user1.getRole());
        assertTrue(encoder.matches(user1.getPassword(),userCaptor.getValue().getPassword()));
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        // given & when
        user1.setId(1);
        when(userService.findById(anyInt())).thenReturn(user1);
        // then
        mockMvc.perform(get("/user/update/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("user/update")).andExpect(model().attribute("user", user1));
    }
    
    @Test
    public void updateUserTest_whenUserValidAndExisting_thenRedirectUserList() throws Exception {
        // when
            user2.setPassword("Jadmin4all&lp4e");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            when(userService.findAll()).thenReturn(new ArrayList(Arrays.asList(user2)));
        // then
        mockMvc.perform(post("/user/update/{id}",1)
            .param("username", user2.getUsername())
            .param("fullname", user2.getFullname())
            .param("email", user2.getEmail())
            .param("role", user2.getRole())
            .param("password", user2.getPassword())
            .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(model().attribute("users",Arrays.asList(user2) ));

                ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userService, times(1)).saveUser(userCaptor.capture());
        assertEquals(userCaptor.getValue().getFullname(), user1.getFullname());
        assertEquals(userCaptor.getValue().getEmail(), user1.getEmail());
        assertEquals(userCaptor.getValue().getRole(), user1.getRole());
        assertEquals(userCaptor.getValue().getId(), 1);
        assertTrue(encoder.matches(user1.getPassword(), userCaptor.getValue().getPassword()));
        
    }

}
