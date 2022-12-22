package com.nnk.springboot.security;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationSecurityTest {
    
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void contextLoads() {
        assertEquals(true,context.containsBean("loginController"));
    }

    @Test

    public void formLoginTest_whenOauth2Authentication() throws Exception {

        mockMvc.perform(get("/").with(oauth2Login())).andExpect(authenticated()).andDo(print());
        
      
    }
    
    @Test
    @Sql({"classpath:data-test.sql"})
    public void formLoginTest_whenUserNamePasswordCorrect_thenUnauthenticated() throws Exception {
        mockMvc.perform(formLogin("/process-login").user("user").password("Jsuser4all&lp"))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/")).andDo(print());
    }
    
    @Test
    @Sql({"classpath:data-test.sql"})
    public void formLoginTest_whenUserNamePasswordNotCorrect_thenUnauthenticatedAndReturnLoginError() throws Exception {
        mockMvc.perform(formLogin("/process-login").user("user").password("incorrectPassword"))
            .andExpect(unauthenticated())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/app/login?error"));   
    }
    
}
