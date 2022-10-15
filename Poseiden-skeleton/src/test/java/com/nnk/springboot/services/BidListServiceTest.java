package com.nnk.springboot.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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
  public void saveBidListTest_whenBidNullExisted() {

    // given
    BidList mockBidList = new BidList("account", "type", 10d);
    BidList mockSavedBidList = mockBidList;
    mockSavedBidList.setBidListId(1);

    when(bidListRepository.save(any(BidList.class))).thenReturn(mockSavedBidList);

    // when
    BidList saveBidList = cut.saveBidList(mockBidList);

    // then
    assertEquals(mockSavedBidList, saveBidList);
  }

  @Test
  public void updateBidListTest_whenBidNull_thenThrowException() {

    Assertions.assertThatThrownBy(() -> {
      cut.updateBidList(null);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-null", spyCaptor.getValue());
  }

  @Test
  public void updateBidListTest_whenBidIdZero_thenThrowException() {

    // given
    BidList bidListToUpdate = new BidList("account", "type", 10d);
    bidListToUpdate.setBidListId(0);

    // when & then
    Assertions.assertThatThrownBy(() -> {
      cut.updateBidList(bidListToUpdate);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-null", spyCaptor.getValue());
  }

  @Test
  public void updateBidListTest_whenBidExisted() {

    // given
    BidList bidListToUpdate = new BidList("account", "type", 10d);
    bidListToUpdate.setBidListId(1);
    BidList mockExistedBidList = new BidList("existedAccount", "existedType", 20d);
    mockExistedBidList.setBidListId(1);

    when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedBidList));
    when(bidListRepository.save(any(BidList.class))).thenReturn(bidListToUpdate);
    // when

    BidList updatedBidList = cut.updateBidList(bidListToUpdate);

    // then
    assertEquals(updatedBidList, bidListToUpdate);
  }

  @Test
  public void updateBidListTest_whenBidNotExisted_ThenThrowBidListNotFoundException() {

    // given
    BidList bidListToUpdate = new BidList("account", "type", 10d);
    bidListToUpdate.setBidListId(1);
    BidList mockExistedBidList = new BidList("existedAccount", "existedType", 20d);
    mockExistedBidList.setBidListId(1);

    when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());

    // when
    assertThrows(BidListNotFoundException.class, () -> {
      cut.updateBidList(bidListToUpdate);
    });

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-found", spyCaptor.getValue());
  }

  @Test
  public void updateBidListTest_whenBidExistedWithNotSameId_ThenThrowBidListNotFoundException() {

    // given
    BidList bidListToUpdate = new BidList("account", "type", 10d);
    bidListToUpdate.setBidListId(1);
    BidList mockExistedBidList = new BidList("existedAccount", "existedType", 20d);
    mockExistedBidList.setBidListId(2);

    when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedBidList));
    when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());

    // when
    assertThrows(BidListNotFoundException.class, () -> {
      cut.updateBidList(bidListToUpdate);
    });

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-found", spyCaptor.getValue());
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
  public void deleteBidListTest_whenBidIdZero_thenThrowException() {

    BidList bid = new BidList("Account Test", "Type Test", 10d);
    bid.setBidListId(0);

    Assertions.assertThatThrownBy(() -> {
      cut.deleteBidList(bid);
    }).isInstanceOf(BidListNotFoundException.class);

    ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
    verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
    assertEquals("global.exception.not-null", spyCaptor.getValue());
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

  @Test
  public void deleteBidListTest_whenBidExist() {

    BidList bid = new BidList("Account Test", "Type Test", 10d);
    bid.setBidListId(1);
    when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(bid));

    cut.deleteBidList(bid);

    verify(bidListRepository, times(1)).delete(any(BidList.class));

  }
  // TODO test for findById
}
