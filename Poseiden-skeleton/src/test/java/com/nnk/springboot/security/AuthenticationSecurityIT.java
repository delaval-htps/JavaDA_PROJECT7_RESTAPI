package com.nnk.springboot.security;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class AuthenticationSecurityIT {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test

    public void formLoginTest_whenOauth2Authentication() throws Exception {
        mockMvc.perform(get("/app/login").with(oauth2Login())).andExpect(authenticated()).andDo(print());   
    }
    
    @Test
    @Sql({"data-test.sql"})
    public void formLoginTest_whenUserNamePasswordNotCorrect_thenUnauthenticated() throws Exception {
        mockMvc.perform(formLogin("/process-login").user("user").password("Jsuser4all&lp"))
            .andExpect(authenticated())
            .andExpect(status().is3xx())
            .andExpect(redirectedUrl("/"));   
    }
}
