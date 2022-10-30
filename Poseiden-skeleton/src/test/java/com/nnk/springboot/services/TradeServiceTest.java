package com.nnk.springboot.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;

@RunWith(MockitoJUnitRunner.class)
public class TradeServiceTest {
   
    @Mock
    private TradeRepository tradeRepository;

    @Spy
    private MessageSource messageSource;

    @InjectMocks
    private TradeService cut;

    @Test
    public void findByIdTrade_whenTradeNotExisted_thenThrowTradeException() {
        // Given

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & then
        Assertions.assertThatThrownBy(() -> {
            cut.findById(1);
        }).isInstanceOf(TradeNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.trade.not-found", spyCaptor.getValue());
    }

    @Test
    public void findByIdTrade_whenTradeExisted_thenReturnTrade() {
        // Given
        Trade mockExistedTrade = new Trade("account", "type");
        mockExistedTrade.setTradeId(1);
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedTrade));

        //when &  then
        Trade findExistedTrade = cut.findById(1);

        assertEquals(findExistedTrade, mockExistedTrade);
    }
    
    @Test
    public void saveTradeTest_whenTradeNull_thenThrowException() {
        Assertions.assertThatThrownBy(() -> {
            cut.saveTrade(null);
        }).isInstanceOf(TradeNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void saveTradeTest_whenTradeNotNull_thenSaveIt() {
        // when
        Trade mockTrade = new Trade("acount", "type");
        Trade mockSavedTrade = mockTrade;
        when(tradeRepository.save(any(Trade.class))).thenReturn(mockSavedTrade);

        // then
        Trade savedTrade = cut.saveTrade(mockTrade);

        assertEquals(savedTrade, mockSavedTrade);
        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.trade.creation", spyCaptor.getValue());
    }
    
    @Test
    public void updateTradeTest_whenTradeIdNotSame_thenThrowTradeException() {

        // when
        Trade mockExistedTrade = new Trade("acount1", "type");
        Trade mockTradeToUpdate = new Trade("acount2", "type");
        mockExistedTrade.setTradeId(1);
        mockTradeToUpdate.setTradeId(2);

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedTrade));

        Assertions.assertThatThrownBy(() -> {
            cut.updateTrade(mockTradeToUpdate);
        }).isInstanceOf(TradeNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.trade.not-found", spyCaptor.getValue());
    }
    @Test
    public void updateTradeTest_whenTradeExisted_thenUpdateTrade() {
        // when
        Trade mockExistedTrade = new Trade("acount1", "type");
        Trade mockTradeToUpdate = new Trade("acount2", "type");
        mockExistedTrade.setTradeId(1);
        mockTradeToUpdate.setTradeId(1);

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedTrade));

        when(tradeRepository.save(any(Trade.class))).thenReturn(mockTradeToUpdate);

        // then
        Trade updatedTrade = cut.updateTrade(mockTradeToUpdate);

        assertEquals(updatedTrade, mockTradeToUpdate);

        verify(tradeRepository, times(1)).save(any(Trade.class));
        verify(tradeRepository, times(1)).findById(anyInt());

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.trade.update", spyCaptor.getValue());
    }

    @Test
    public void updateTradeTest_whenTradeNoExisted_thenUpdateTrade() {
        // when
        Trade mockNotExistedTrade = new Trade("acount1", "type");
        Trade mockTradeToUpdate =  new Trade("acount2", "type");
        mockNotExistedTrade.setTradeId(1);
        mockTradeToUpdate.setTradeId(1);

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.updateTrade(mockTradeToUpdate);
        }).isInstanceOf(TradeNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.trade.not-found", spyCaptor.getValue());
    }

    @Test
    public void deleteTradeTest_whenTradeNull_thenThrowTradeException() {
        Assertions.assertThatThrownBy(() -> {
            cut.deleteTrade(null);
        }).isInstanceOf(TradeNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteTradeTest_whenTradeIdZero_thenThrowTradeException() {

        // when
        Trade mockNotExistedTrade =  new Trade("acount1", "type");
        mockNotExistedTrade.setTradeId(0);

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteTrade(mockNotExistedTrade);
        }).isInstanceOf(TradeNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteTradeTest_whenTradeExisted_thenDeleteTrade() {
        // when
        Trade mockExistedTrade =  new Trade("acount1", "type");
        mockExistedTrade.setTradeId(1);

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedTrade));

        // then
        cut.deleteTrade(mockExistedTrade);
        verify(tradeRepository, times(1)).delete(any(Trade.class));
    }

    @Test
    public void deleteTradeTest_whenTradeNotExisted_thenDeleteTrade() {
        // when
        Trade mockNotExistedTrade =  new Trade("acount1", "type");
        mockNotExistedTrade.setTradeId(1);

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteTrade(mockNotExistedTrade);
        }).isInstanceOf(TradeNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.trade.not-found", spyCaptor.getValue());
    }

}
