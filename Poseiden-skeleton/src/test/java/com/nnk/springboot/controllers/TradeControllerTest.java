package com.nnk.springboot.controllers;

import static org.junit.Assert.assertTrue;
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

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.services.TradeService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = TradeController.class)
public class TradeControllerTest {
    @MockBean
    private TradeService tradeService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    List<Trade> trades;
    Trade mockTrade1;
    Trade mockTrade2;

    @Before
    public void initialize() {
        trades = new ArrayList<>();
        mockTrade1 = new Trade("account1", "type1");
        mockTrade2 = new Trade("account2", "type2");
        trades.add(mockTrade1);
        trades.add(mockTrade2);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void homeTest() throws Exception {
        
        when(tradeService.findAll()).thenReturn(trades);

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attribute("listOfTrade", trades));
    }

    @Test
    public void addTradeFormTest() throws Exception {

        mockMvc.perform(get("/trade/add")).andExpect(status().isOk()).andExpect(view().name("trade/add"));

    }

    @Test
    public void validateTest_whenTradeValid_ThenReturnTrade() throws Exception {

        // given
        Trade registredmockTrade1 = mockTrade1;
        registredmockTrade1.setTradeId(1);

        when(tradeService.saveTrade(Mockito.any(Trade.class))).thenReturn(registredmockTrade1);

        // when & then
        mockMvc.perform(post("/trade/validate").param("account", "account").param("type", "type").param("buyQuantity","10.0").with(csrf()))

                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).saveTrade(Mockito.any(Trade.class));

    }

    @Test
    public void validateTest_whenTradeNotValid_ThenReturnTradeAdd() throws Exception {

        // when & then
        mockMvc.perform(post("/trade/validate").param("name", "").param("description", "").param("json", "").param("sqlStr", "").param("sqlPart", "").with(csrf())).andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));

        verify(tradeService, never()).saveTrade(Mockito.any(Trade.class));

    }

    @Test
    public void showUpdateFormTest() throws Exception {
        
        //given
        when(tradeService.findById(Mockito.anyInt())).thenReturn(mockTrade1);

        //when  & then
        mockMvc.perform(get("/trade/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("trade",mockTrade1))
                .andExpect(view().name("trade/update"));
        
        verify(tradeService, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void showUpdateForm_whenIdZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/trade/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));
    }

    @Test
    public void updateTrade_whenTradeNotValid_thenReturnToShowUpdate() throws Exception {

        mockMvc.perform(post("/trade/update/{id}", 1).param("account", "").param("type", "").with(csrf())).andExpect(view().name("trade/update")).andExpect(model().attributeExists("trade"));

        verify(tradeService, never()).updateTrade(Mockito.any(Trade.class));

    }

    @Test
    public void updateTrade_whenTradeValid_thenReturnToShowUpdate() throws Exception {

        // given
        mockTrade2.setTradeId(1);
        when(tradeService.updateTrade(Mockito.any(Trade.class))).thenReturn(mockTrade2);

        mockMvc.perform(post("/trade/update/{id}", mockTrade2.getTradeId()).param("account", mockTrade2.getAccount()).param("type", mockTrade2.getType()).param("buyQuantity","10.0").with(csrf()))
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).updateTrade(Mockito.any(Trade.class));

    }

    @Test
    public void updateTrade_whenTradeIdZero_thenThrowGlobalPoseidonException() throws Exception {

        // given
        mockTrade2.setTradeId(0);
        when(tradeService.updateTrade(Mockito.any(Trade.class))).thenReturn(mockTrade2);

        mockMvc.perform(post("/trade/update/{id}", mockTrade2.getTradeId()).param("account", mockTrade2.getAccount()).param("type", mockTrade2.getType()).with(csrf()))
                .andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));
        verify(tradeService, never()).updateTrade(Mockito.any(Trade.class));

    }

    @Test
    public void deleteTrade_whenIdIsZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/trade/delete/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }

    @Test
    public void deleteTrade_whenTradeExists_thenReturnToTradeList() throws Exception {

        // when
        mockTrade1.setTradeId(1);
        when(tradeService.findById(Mockito.anyInt())).thenReturn(mockTrade1);

        // then
        mockMvc.perform(get("/trade/delete/{id}", mockTrade1.getTradeId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).deleteTrade(Mockito.any(Trade.class));
    }

    @Test
    public void deleteTrade_whenTradeNotExists_thenThrowTradeNotFoundException() throws Exception {

        // when ( the service throws exception if rating is not found
        // with id by défault )
        mockTrade1.setTradeId(1);
        when(tradeService.findById(Mockito.anyInt())).thenThrow(new TradeNotFoundException("id not found"));

        // then
        mockMvc.perform(get("/trade/delete/{id}", mockTrade1.getTradeId())).andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TradeNotFoundException));

        verify(tradeService, never()).deleteTrade(Mockito.any(Trade.class));
    }

}
