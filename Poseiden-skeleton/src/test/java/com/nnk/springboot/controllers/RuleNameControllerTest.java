package com.nnk.springboot.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.services.RuleNameService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = RuleNameController.class)
public class RuleNameControllerTest {

    @MockBean
    private RuleNameService ruleNameService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    List<RuleName> rules;
    RuleName mockRuleName1;
    RuleName mockRuleName2;

    @Before
    public void initialize() {
        rules = new ArrayList<>();
        mockRuleName1 = new RuleName("ruleName", "description", "jsonString", "template", "sqlStr", "sqlPart");
        mockRuleName2 = new RuleName("ruleName2", "description", "jsonString", "template", "sqlStr", "sqlPart");
        rules.add(mockRuleName1);
        rules.add(mockRuleName2);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void homeTest() throws Exception {
        
        when(ruleNameService.findAll()).thenReturn(rules);

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attribute("listOfRuleName", rules));
    }

    @Test
    public void addRuleNameFormTest() throws Exception {

        mockMvc.perform(get("/ruleName/add")).andExpect(status().isOk()).andExpect(view().name("ruleName/add"));

    }

    @Test
    public void validateTest_whenRuleNameValid_ThenReturnRuleName() throws Exception {

        // given
        RuleName registredmockRuleName1 = mockRuleName1;
        registredmockRuleName1.setId(1);

        when(ruleNameService.saveRuleName(Mockito.any(RuleName.class))).thenReturn(registredmockRuleName1);

        // when & then
        mockMvc.perform(post("/ruleName/validate").param("name", "ruleName1").param("description", "description").param("json", "jsonString").param("template","template").param("sqlStr", "sqlString").param("sqlPart", "sqlPart")
                .with(csrf()))

                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).saveRuleName(Mockito.any(RuleName.class));

    }

    @Test
    public void validateTest_whenRuleNameNotValid_ThenReturnRuleNameAdd() throws Exception {

        // when & then
        mockMvc.perform(post("/ruleName/validate").param("name", "").param("description", "").param("json", "").param("sqlStr", "").param("sqlPart", "").with(csrf()))
                .andExpect(view().name("ruleName/add")).andExpect(model().attributeExists("ruleName"));

        verify(ruleNameService, never()).saveRuleName(Mockito.any(RuleName.class));

    }

    @Test
    public void showUpdateFormTest() throws Exception {
        
        //given
        when(ruleNameService.findById(Mockito.anyInt())).thenReturn(mockRuleName1);

        //when  & then
        mockMvc.perform(get("/ruleName/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("ruleName",mockRuleName1))
                .andExpect(view().name("ruleName/update"));
        
        verify(ruleNameService, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void showUpdateForm_whenIdZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/ruleName/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));
    }

    @Test
    public void updateRuleName_whenRuleNameNotValid_thenReturnToShowUpdate() throws Exception {

        mockMvc.perform(post("/ruleName/update/{id}", 1).param("name", "").param("description", "").param("json", "").param("sqlStr", "").param("sqlPart", "").with(csrf()))
                .andExpect(view().name("ruleName/update")).andExpect(model().attributeExists("ruleName"));

        verify(ruleNameService, never()).updateRuleName(Mockito.any(RuleName.class));

    }

    @Test
    public void updateRuleName_whenRuleNameValid_thenReturnToShowUpdate() throws Exception {

        // given
        mockRuleName2.setId(1);
        when(ruleNameService.updateRuleName(Mockito.any(RuleName.class))).thenReturn(mockRuleName2);

        mockMvc.perform(post("/ruleName/update/{id}", mockRuleName2.getId()).param("name", mockRuleName2.getName()).param("description", mockRuleName2.getDescription())
                .param("template", mockRuleName2.getTemplate()).param("json", mockRuleName2.getJson()).param("sqlStr", mockRuleName2.getSqlStr()).param("sqlPart", mockRuleName2.getSqlPart())
                .with(csrf())).andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).updateRuleName(Mockito.any(RuleName.class));

    }
    @Test
    public void deleteRuleName_whenIdIsZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/ruleName/delete/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }
    @Test
    public void deleteRuleName_whenRuleNameExists_thenReturnToRuleNameList() throws Exception {

        // when
        mockRuleName1.setId(1);
        when(ruleNameService.findById(Mockito.anyInt())).thenReturn(mockRuleName1);

        // then
        mockMvc.perform(get("/ruleName/delete/{id}", mockRuleName1.getId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).deleteRuleName(Mockito.any(RuleName.class));
    }

    @Test
    public void deleteRuleName_whenRuleNameNotExists_thenThrowRuleNameNotFoundException() throws Exception {

        // when ( the service throws exception if rating is not found
        // with id by défault )
        mockRuleName1.setId(1);
        when(ruleNameService.findById(Mockito.anyInt())).thenThrow(new RuleNameNotFoundException("id not found"));

        // then
        mockMvc.perform(get("/ruleName/delete/{id}", mockRuleName1.getId())).andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuleNameNotFoundException));

        verify(ruleNameService, never()).deleteRuleName(Mockito.any(RuleName.class));
    }
}
