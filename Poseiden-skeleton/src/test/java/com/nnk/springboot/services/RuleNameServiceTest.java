package com.nnk.springboot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;

@RunWith(MockitoJUnitRunner.class)
public class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @Spy
    private MessageSource messageSource;

    @InjectMocks
    private RuleNameService cut;

    private List<RuleName> ruleNames;
    private RuleName mockRuleName1, mockRuleName2;

    @Before
    public void intialize() {
        ruleNames = new ArrayList<>();
        mockRuleName1 = new RuleName("ruleName1", "description", "jsonString","template","sqlString","sqlPart");
        mockRuleName2 = new RuleName("ruleName2", "description", "jsonString","template","sqlString","sqlPart");
        ruleNames.add(mockRuleName1);
        ruleNames.add(mockRuleName2);
    }

    @Test
      public void findAll() {
          when(ruleNameRepository.findAll()).thenReturn(ruleNames);
          List<RuleName> findAll = cut.findAll();
          assertEquals(findAll, ruleNames);
      }
    @Test
    public void findByIdRuleName_whenRuleNameNotExisted_thenThrowRuleNameException() {
        // when

        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.findById(1);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rule-name.not-found", spyCaptor.getValue());
    }

    @Test
    public void findByIdRuleName_whenRuleNameExisted_thenReturnRuleName() {
        // when
        RuleName mockExistedRuleName = new RuleName("ruleName1", "description", "jsonString","template","sqlString","sqlPart");
        mockExistedRuleName.setId(1);
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedRuleName));

        // then
        RuleName findExistedRuleName = cut.findById(1);
        
        assertEquals(findExistedRuleName, mockExistedRuleName);
    }
    @Test
    public void saveRuleNameTest_whenRuleNameNull_thenThrowException() {
        Assertions.assertThatThrownBy(() -> {
            cut.saveRuleName(null);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void saveRuleNameTest_whenRuleNameNotNull_thenSaveIt() {
        // when
        RuleName mockRuleName = new RuleName("ruleName1", "description", "jsonString", "template", "sqlString", "sqlPart");
        RuleName mockSavedRuleName = mockRuleName;
        when(ruleNameRepository.save(Mockito.any(RuleName.class))).thenReturn(mockSavedRuleName);

        // then
        RuleName savedRuleName = cut.saveRuleName(mockRuleName);

        assertEquals(savedRuleName, mockSavedRuleName);
        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rule-name.creation", spyCaptor.getValue());
    }
    
    @Test
    public void updateRuleNameTest_whenUserNull_thenThrowUserException() {
        Assertions.assertThatThrownBy(() -> {
            cut.updateRuleName(null);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void updateRuleNameTest_whenUserIdZero_thenThrowUserException() {
        mockRuleName1.setId(0);
        Assertions.assertThatThrownBy(() -> {
            cut.updateRuleName(mockRuleName1);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void updateRuleNameTest_whenRuleNameIdNotSame_thenThrowRuleNameException() {

        // when
        RuleName mockExistedRuleName = new RuleName("ruleName1", "description", "jsonString", "template", "sqlString", "sqlPart");
        RuleName mockRuleNameToUpdate = new RuleName("ruleName2", "description", "jsonString", "template", "sqlString", "sqlPart");
        mockExistedRuleName.setId(1);
        mockRuleNameToUpdate.setId(2);

        when(ruleNameRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockExistedRuleName));

        Assertions.assertThatThrownBy(() -> {
            cut.updateRuleName(mockRuleNameToUpdate);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rule-name.not-found", spyCaptor.getValue());
    }
    @Test
    public void updateRuleNameTest_whenRuleNameExisted_thenUpdateRuleName() {
        // when
        RuleName mockExistedRuleName = new RuleName("ruleName1", "description", "jsonString", "template", "sqlString", "sqlPart");
        RuleName mockRuleNameToUpdate = new RuleName("ruleName2", "description", "jsonString", "template", "sqlString", "sqlPart");
        mockExistedRuleName.setId(1);
        mockRuleNameToUpdate.setId(1);

        when(ruleNameRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockExistedRuleName));

        when(ruleNameRepository.save(Mockito.any(RuleName.class))).thenReturn(mockRuleNameToUpdate);

        // then
        RuleName updatedRuleName = cut.updateRuleName(mockRuleNameToUpdate);

        assertEquals(updatedRuleName, mockRuleNameToUpdate);

        verify(ruleNameRepository, times(1)).save(Mockito.any(RuleName.class));
        verify(ruleNameRepository, times(1)).findById(anyInt());

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rule-name.update", spyCaptor.getValue());
    }

    @Test
    public void updateRuleNameTest_whenRuleNameNoExisted_thenUpdateRuleName() {
        // when
        RuleName mockNotExistedRuleName = new RuleName("ruleName1", "description", "jsonString", "template", "sqlString", "sqlPart");
        RuleName mockRuleNameToUpdate = new RuleName("ruleName2", "description", "jsonString", "template", "sqlString", "sqlPart");
        mockNotExistedRuleName.setId(1);
        mockRuleNameToUpdate.setId(1);

        when(ruleNameRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.updateRuleName(mockRuleNameToUpdate);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rule-name.not-found", spyCaptor.getValue());
    }

    @Test
    public void deleteRuleNameTest_whenRuleNameNull_thenThrowRuleNameException() {
        Assertions.assertThatThrownBy(() -> {
            cut.deleteRuleName(null);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteRuleNameTest_whenRuleNameIdZero_thenThrowRuleNameException() {

        // when
        RuleName mockNotExistedRuleName = new RuleName("ruleName1", "description", "jsonString", "template", "sqlString", "sqlPart");
        mockNotExistedRuleName.setId(0);

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteRuleName(mockNotExistedRuleName);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteRuleNameTest_whenRuleNameExisted_thenDeleteRuleName() {
        // when
        RuleName mockExistedRuleName = new RuleName("ruleName1", "description", "jsonString", "template", "sqlString", "sqlPart");
        mockExistedRuleName.setId(1);

        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedRuleName));

        // then
        cut.deleteRuleName(mockExistedRuleName);
        verify(ruleNameRepository, times(1)).delete(Mockito.any(RuleName.class));
    }

    @Test
    public void deleteRuleNameTest_whenRuleNameNotExisted_thenDeleteRuleName() {
        // when
        RuleName mockNotExistedRuleName = new RuleName("ruleName1", "description", "jsonString", "template", "sqlString", "sqlPart");
        mockNotExistedRuleName.setId(1);

        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteRuleName(mockNotExistedRuleName);
        }).isInstanceOf(RuleNameNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rule-name.not-found", spyCaptor.getValue());
    }

}
