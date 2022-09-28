package com.nnk.springboot.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;

@RunWith(MockitoJUnitRunner.class)
public class BidListServiceTest {

  @Mock
  private BidListRepository bidListRepository;

  @Spy
  private MessageSource messageSource;

  @InjectMocks
  private BidListService cut;

  @Test
  public void saveBidListTest_whenBidNull_thenThrowException() {

    // Save
    Assertions.assertThatThrownBy(() -> {
      cut.saveBidList(null);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-found", spyCaptor.getValue());

  }

  @Test
  public void updateBidListTest_whenBidNull_thenThrowException() {

    // Update
    Assertions.assertThatThrownBy(() -> {
      cut.updateBidList(null);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-null", spyCaptor.getValue());
  }

  @Test
  public void deleteBidListTest_whenBidNull_thenThrowException() {

    Assertions.assertThatThrownBy(() -> {
      cut.deleteBidList(null);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-null", spyCaptor.getValue());
  }

  

  @Test
  public void updateBidListTest_whenNotExist() {

    BidList bid = new BidList("Account Test", "Type Test", 10d);
    bid.setBidListId(1);
    when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());
    // Update
    Assertions.assertThatThrownBy(() -> {
      cut.updateBidList(bid);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-found", spyCaptor.getValue());
  }

  @Test
  public void deleteBidListTest_whenNotExist() {

    BidList bid = new BidList("Account Test", "Type Test", 10d);
    bid.setBidListId(1);
    when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> {
      cut.deleteBidList(bid);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-found", spyCaptor.getValue());

  }
  //TODO test not null and existing bid
}
