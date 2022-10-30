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

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.services.RatingService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = RatingController.class)
public class RatingControllerTest {
    @MockBean
    private RatingService ratingService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    List<Rating> ratings;
    Rating mockRating1;
    Rating mockRating2;

    @Before
    public void initialize() {
        ratings = new ArrayList<Rating>();
        mockRating1 = new Rating("moodysRating", "sandRating", "fitchRating", 1);
        mockRating2 = new Rating("moodysRating", "sandRating", "fitchRating", 2);
        ratings.add(mockRating1);
        ratings.add(mockRating2);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void homeTest() throws Exception {
        
        when(ratingService.findAll()).thenReturn(ratings);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attribute("listOfRating", ratings));
    }

    @Test
    public void addRatingFormTest() throws Exception {

        mockMvc.perform(get("/rating/add")).andExpect(status().isOk()).andExpect(view().name("rating/add"));

    }

    @Test
    public void validateTest_whenRatingValid_ThenReturnRating() throws Exception {

        // given
        Rating registredmockRating1 = mockRating1;
        registredmockRating1.setId(1);

        when(ratingService.saveRating(Mockito.any(Rating.class))).thenReturn(registredmockRating1);

        // when & then
        mockMvc.perform(post("/rating/validate").param("moddysRating", "moddysRating").param("sandPRating", "sandPRating").param("fitchRating", "fitchRating").param("orderNumber", "1").with(csrf()))

                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).saveRating(Mockito.any(Rating.class));

    }

    @Test
    public void validateTest_whenRatingNotValid_ThenReturnRatingAdd() throws Exception {

        // when & then
        mockMvc.perform(post("/rating/validate").param("moddysRating", "moddysRating").param("sandPRating", "sandPRating").param("fitchRating", "fitchRating").param("orderNumber", "1").with(csrf()))
                .andExpect(view().name("rating/add")).andExpect(model().attributeExists("rating"));

        verify(ratingService, never()).saveRating(Mockito.any(Rating.class));

    }

    @Test
    public void showUpdateFormTest() throws Exception {
        
        //given
        when(ratingService.findById(Mockito.anyInt())).thenReturn(mockRating1);

        //when  & then
        mockMvc.perform(get("/rating/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("rating",mockRating1))
                .andExpect(view().name("rating/update"));
        
        verify(ratingService, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void showUpdateForm_whenIdZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/rating/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }


    @Test
    public void updateRating_whenIdIsZero_thenTrhowGlobalPoseidonException() throws Exception {

        mockMvc.perform(post("/rating/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }

    @Test
    public void updateRating_whenRatingNotValid_thenReturnToShowUpdate() throws Exception {

        mockMvc.perform(post("/rating/update/{id}", 1).param("moddysRating", "").param("sandPRating", "").param("fitchRating", "").param("orderNumber", "").with(csrf()))
                .andExpect(view().name("rating/update")).andExpect(model().attributeExists("rating"));

        verify(ratingService, never()).updateRating(Mockito.any(Rating.class));

    }
    @Test
    public void updateRating_whenRatingValide_thenReturnToShowUpdate() throws Exception {

        // given

        mockRating1.setId(1);
        when(ratingService.updateRating(Mockito.any(Rating.class))).thenReturn(mockRating2);

        mockMvc.perform(post("/rating/update/{id}", mockRating1.getId()).param("moodysRating", mockRating1.getMoodysRating()).param("sandPRating", mockRating1.getSandPRating())
                .param("fitchRating", mockRating1.getFitchRating()).param("orderNumber", mockRating1.getOrderNumber().toString()).with(csrf())).andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).updateRating(Mockito.any(Rating.class));

    }
    
    @Test
    public void deleteRating_whenIdIsZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/rating/delete/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }
    @Test
    public void deleteRating_whenRatingExists_thenReturnToRatingList() throws Exception {

        // when
        mockRating1.setId(1);
        when(ratingService.findById(Mockito.anyInt())).thenReturn(mockRating1);

        // then
        mockMvc.perform(get("/rating/delete/{id}", mockRating1.getId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).deleteRating(Mockito.any(Rating.class));
    }

    @Test
    public void deleteRating_whenRatingNotExists_thenThrowRatingNotFoundException() throws Exception {

        // when ( the service throws exception if rating is not found
        // with id by défault )
        mockRating1.setId(1);
        when(ratingService.findById(Mockito.anyInt())).thenThrow(new RatingNotFoundException("id not found"));

        // then
        mockMvc.perform(get("/rating/delete/{id}", mockRating1.getId())).andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RatingNotFoundException));

        verify(ratingService, never()).deleteRating(Mockito.any(Rating.class));
    }

}
