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

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.BidListService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = BidListController.class)
public class BidListControllerTest {

    @MockBean
    private BidListService bidListService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    List<BidList> listOfBidList;
    BidList mockBidList1;
    BidList mockBidList2;

    @Before
    public void initialize() {
        listOfBidList = new ArrayList<BidList>();
        mockBidList1 = new BidList("account1", "type1", 10d);
        mockBidList2 = new BidList("account2", "type2", 20d);
        listOfBidList.add(mockBidList2);
        listOfBidList.add(mockBidList1);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void homeTest() throws Exception {
        
        when(bidListService.findBidLists()).thenReturn(listOfBidList);

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attribute("listOfBidList", listOfBidList));
    }

    @Test
    public void addBidFormTest() throws Exception {

        mockMvc.perform(get("/bidList/add")).andExpect(status().isOk()).andExpect(view().name("bidList/add"));

    }

    @Test
    public void validateTest_whenBidListValid_ThenReturnSavedBidList() throws Exception {

        // given
        BidList registredMockBidList1 = mockBidList1;
        registredMockBidList1.setBidListId(1);
        when(bidListService.saveBidList(Mockito.any(BidList.class))).thenReturn(registredMockBidList1);

        // when & then
        mockMvc.perform(post("/bidList/validate").param("account", "account").param("type", "type").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).saveBidList(Mockito.any(BidList.class));

    }

    @Test
    public void validateTest_whenBidListNotValid_ThenReturnBidListAdd() throws Exception {

        // when & then
        mockMvc.perform(post("/bidList/validate").param("account", "").param("type", "").with(csrf())).andExpect(view().name("bidList/add")).andExpect(model().attributeExists("bidList"));

        verify(bidListService, never()).saveBidList(Mockito.any(BidList.class));

    }

    @Test
    public void showUpdateFormTest() throws Exception {
        //given
        when(bidListService.findById(Mockito.anyInt())).thenReturn(mockBidList1);
        //when  & then
        mockMvc.perform(get("/bidList/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bidList",mockBidList1))
                .andExpect(view().name("bidList/update"));
        
        verify(bidListService, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void showUpdateForm_whenIdZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/bidList/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }

    @Test
    public void updateBid_whenIdIsZero_thenTrhowGlobalPoseidonException() throws Exception {

        mockMvc.perform(post("/bidList/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }
    
    @Test
    public void updateBid_whenbidListNotValide_thenReturnToShowUpdate() throws Exception {

        mockMvc.perform(post("/bidList/update/{id}", 1).param("account", "").param("type", "").with(csrf())).andExpect(view().name("bidList/update")).andExpect(model().attributeExists("bidList"));

        verify(bidListService, never()).updateBidList(Mockito.any(BidList.class));

    }

    @Test
    public void updateBid_whenbidListValide_thenReturnToShowUpdate() throws Exception {

        // given
       
        mockBidList1.setBidListId(1);
        when(bidListService.updateBidList(Mockito.any(BidList.class))).thenReturn(mockBidList2);

        mockMvc.perform(post("/bidList/update/{id}", mockBidList1.getBidListId()).param("account", mockBidList1.getAccount()).param("type", mockBidList1.getType()).with(csrf())).andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).updateBidList(Mockito.any(BidList.class));

    }

    @Test
    public void deleteBid_whenIdIsZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/bidList/delete/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }

    @Test
    public void deleteBid_whenBidListExists_thenReturnToBidList() throws Exception {
       
        //when
        mockBidList1.setBidListId(1);
        when(bidListService.findById(Mockito.anyInt())).thenReturn(mockBidList1);
        
        //then
        mockMvc.perform(get("/bidList/delete/{id}", mockBidList1.getBidListId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).deleteBidList(Mockito.any(BidList.class));
    }
    @Test
    public void deleteBid_whenBidListNotExists_thenThrowBidListNotFoundException() throws Exception {
        
        //when ( the service throws exception if bidList is not found with id by défault )
        mockBidList1.setBidListId(1);
        when(bidListService.findById(Mockito.anyInt())).thenThrow(new BidListNotFoundException("id not found"));
        
        //then
        mockMvc.perform(get("/bidList/delete/{id}", mockBidList1.getBidListId())).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof BidListNotFoundException));

        verify(bidListService, never()).deleteBidList(Mockito.any(BidList.class));
    }

}
