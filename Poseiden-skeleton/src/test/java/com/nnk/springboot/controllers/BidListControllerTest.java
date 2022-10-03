package com.nnk.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers =BidListController.class)
public class BidListControllerTest {

    @MockBean 
    private BidListService bidListService;

    @Autowired
    private WebApplicationContext webApplicationContext;
 
    private MockMvc mockMvc;

    List<BidList> listOfBidList;

    @Before
    public void initialize() {
        listOfBidList = new ArrayList();
        BidList mockBidList1 = new BidList("account1", "type1", 10d);
        BidList mockBidList2 = new BidList("account2", "type2", 20d);
        listOfBidList.add(mockBidList2);
        listOfBidList.add(mockBidList1);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
     }

    @Test
    public void home() throws Exception {
        
        when(bidListService.findBidLists()).thenReturn(listOfBidList);

        mockMvc.perform(get("/bidList/list")).andExpect(status().isOk()).andExpect(model().attribute("listOfBidList", listOfBidList));
    }
    
}
