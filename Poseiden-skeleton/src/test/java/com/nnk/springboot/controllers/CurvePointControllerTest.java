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

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.CurvePointService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(controllers = CurveController.class)
public class CurvePointControllerTest {

    @MockBean
    private CurvePointService curvePointService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    List<CurvePoint> curvePoints;
    CurvePoint mockCurvePoint1;
    CurvePoint mockCurvePoint2;

    @Before
    public void initialize() {
        curvePoints = new ArrayList<CurvePoint>();
        mockCurvePoint1 = new CurvePoint(1, 10.0d, 10d);
        mockCurvePoint2 = new CurvePoint(2, 20d, 20d);
        curvePoints.add(mockCurvePoint2);
        curvePoints.add(mockCurvePoint1);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void homeTest() throws Exception {
        
        when(curvePointService.findAll()).thenReturn(curvePoints);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attribute("listOfCurvePoint", curvePoints));
    }

    @Test
    public void addCurveFormTest() throws Exception {

        mockMvc.perform(get("/curvePoint/add")).andExpect(status().isOk()).andExpect(view().name("curvePoint/add"));

    }

    @Test
    public void validateTest_whenCurvePointValid_ThenReturnCurvePoint() throws Exception {

        // given
        CurvePoint registredmockCurvePoint1 = mockCurvePoint1;
        registredmockCurvePoint1.setId(1);

        when(curvePointService.saveCurvePoint(Mockito.any(CurvePoint.class))).thenReturn(registredmockCurvePoint1);

        // when & then
        mockMvc.perform(post("/curvePoint/validate").param("curveId","1").param("term", "10.0d").param("value", "10d").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).saveCurvePoint(Mockito.any(CurvePoint.class));

    }

    @Test
    public void validateTest_whenCurvePointNotValid_ThenReturnCurvePointAdd() throws Exception {

        // when & then
        mockMvc.perform(post("/curvePoint/validate").param("term", "").param("value", "").with(csrf())).andExpect(view().name("curvePoint/add")).andExpect(model().attributeExists("curvePoint"));

        verify(curvePointService, never()).saveCurvePoint(Mockito.any(CurvePoint.class));

    }

    @Test
    public void showUpdateFormTest() throws Exception {
        
        //given
        when(curvePointService.findById(Mockito.anyInt())).thenReturn(mockCurvePoint1);

        //when  & then
        mockMvc.perform(get("/curvePoint/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("curvePoint",mockCurvePoint1))
                .andExpect(view().name("curvePoint/update"));
        
        verify(curvePointService, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void showUpdateForm_whenIdZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/curvePoint/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }

    @Test
    public void updateCurvePoint_whenIdIsZero_thenTrhowGlobalPoseidonException() throws Exception {

        mockMvc.perform(post("/curvePoint/update/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }

    @Test
    public void updateCurvePoint_whenCurvePointNotValide_thenReturnToShowUpdate() throws Exception {

        mockMvc.perform(post("/curvePoint/update/{id}", 1).param("term", "").param("value", "").with(csrf())).andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));

        verify(curvePointService, never()).updateCurvePoint(Mockito.any(CurvePoint.class));

    }

    @Test
    public void updateCurvePoint_whenCurvePointValide_thenReturnToShowUpdate() throws Exception {

        // given

        mockCurvePoint1.setId(1);
        when(curvePointService.updateCurvePoint(Mockito.any(CurvePoint.class))).thenReturn(mockCurvePoint2);

        mockMvc.perform(post("/curvePoint/update/{id}", mockCurvePoint1.getId()).param("curveId", mockCurvePoint1.getCurveId().toString()).param("term", mockCurvePoint1.getTerm().toString()).param("value", mockCurvePoint1.getValue().toString()).with(csrf()))
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).updateCurvePoint(Mockito.any(CurvePoint.class));

    }

    @Test
    public void deleteCurvePoint_whenIdIsZero_thenThrowGlobalPoseidonException() throws Exception {

        mockMvc.perform(get("/curvePoint/delete/{id}", 0)).andExpect(status().is4xxClientError()).andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalPoseidonException));

    }

    @Test
    public void deleteCurvePoint_whencurvePointExists_thenReturnTocurvePoint() throws Exception {

        // when
        mockCurvePoint1.setId(1);
        when(curvePointService.findById(Mockito.anyInt())).thenReturn(mockCurvePoint1);

        // then
        mockMvc.perform(get("/curvePoint/delete/{id}", mockCurvePoint1.getId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).deleteCurvePoint(Mockito.any(CurvePoint.class));
    }

    @Test
    public void deleteCurvePoint_whencurvePointNotExists_thenThrowcurvePointNotFoundException() throws Exception {

        // when ( the service throws exception if curvePoint is not found
        // with id by défault )
        mockCurvePoint1.setId(1);
        when(curvePointService.findById(Mockito.anyInt())).thenThrow(new CurvePointNotFoundException("id not found"));

        // then
        mockMvc.perform(get("/curvePoint/delete/{id}", mockCurvePoint1.getId())).andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CurvePointNotFoundException));

        verify(curvePointService, never()).deleteCurvePoint(Mockito.any(CurvePoint.class));
    }

}
