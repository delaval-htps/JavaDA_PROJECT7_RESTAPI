package com.nnk.springboot.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.RequestDispatcher;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = CustomErrorController.class)

public class CustomErrorControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void initialize() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void handleError_WhenErrorStatusNull_ThenReturnInternalErrorWithoutStatusCode() throws Exception {
        mockMvc.perform(get("/app/internal-error"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorAttributes"))
                .andExpect(model().attributeDoesNotExist("statusCode"))
                .andDo(print());
    }

    @Test
    public void handleError_whenStatusNotNull_ThenReturnInternalErrorWithStatusCode() throws Exception {

        mockMvc.perform(get("/app/internal-error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE,
                        400)
                .requestAttr(RequestDispatcher.ERROR_MESSAGE,
                        "erreur Message")
                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "http://localhost:8080//bidlist/update/100"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorAttributes", Matchers.hasEntry("message", "erreur Message")))
                .andExpect(model().attribute("errorAttributes",
                        Matchers.hasEntry("path", "http://localhost:8080//bidlist/update/100")))
                .andExpect(model().attribute("errorAttributes", Matchers.hasEntry("error", "Bad Request")))
                .andExpect(model().attribute("errorAttributes", Matchers.hasKey("timestamp")))
                .andExpect(model().attribute("statusCode", "400"));

    }

}