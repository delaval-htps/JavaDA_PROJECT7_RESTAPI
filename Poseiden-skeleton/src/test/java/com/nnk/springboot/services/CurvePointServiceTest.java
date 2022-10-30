package com.nnk.springboot.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import java.util.Locale;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

@RunWith(MockitoJUnitRunner.class)
public class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @Spy
    private MessageSource messageSource;

    @InjectMocks
    private CurvePointService cut;

    @Test
    public void findByIdCurvePoint_whenCurvePointNotExisted_thenThrowCurvePointException() {
        // when

        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.findById(1);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.curve-point.not-found", spyCaptor.getValue());
    }

    @Test
    public void findByIdCurvePoint_whenCurvePointExisted_thenThrowCurvePointException() {
        // when
        CurvePoint mockExistedCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        mockExistedCurvePoint.setId(1);
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedCurvePoint));

        // then
        CurvePoint findExistedCurvePoint = cut.findById(1);
        
        assertEquals(findExistedCurvePoint, mockExistedCurvePoint);
    }

    @Test
    public void saveCurvePointTest_whenCurvePointNull_thenThrowException() {
        Assertions.assertThatThrownBy(() -> {
            cut.saveCurvePoint(null);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void saveCurvePointTest_whenCurvePointNotNull_thenSaveIt() {
        // when
        CurvePoint mockCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        CurvePoint mockSavedCurvePoint = mockCurvePoint;
        when(curvePointRepository.save(Mockito.any(CurvePoint.class))).thenReturn(mockSavedCurvePoint);

        // then
        CurvePoint savedCurvePoint = cut.saveCurvePoint(mockCurvePoint);

        assertEquals(savedCurvePoint, mockSavedCurvePoint);
        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.curve-point.creation", spyCaptor.getValue());
    }

    @Test
    public void updateCurvePointTest_whenCurvePointNull_thenThrowCurvePointException() {
        Assertions.assertThatThrownBy(() -> {
            cut.updateCurvePoint(null);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void updateCurvePointTest_whenCurvePointIdNotSame_thenThrowCurvePointException() {

        // when
        CurvePoint mockExistedCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        CurvePoint mockUpdatedCurvePoint = new CurvePoint(1, 13.0d, 1.0d);
        mockExistedCurvePoint.setId(1);
        mockUpdatedCurvePoint.setId(2);

        when(curvePointRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockExistedCurvePoint));

        Assertions.assertThatThrownBy(() -> {
            cut.updateCurvePoint(mockUpdatedCurvePoint);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.curve-point.not-found", spyCaptor.getValue());
    }

    @Test
    public void updateCurvePointTest_whenCurvePointExisted_thenUpdateCurvePoint() {
        // when
        CurvePoint mockExistedCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        CurvePoint mockUpdatedCurvePoint = new CurvePoint(1, 13.0d, 1.0d);
        mockExistedCurvePoint.setId(1);
        mockUpdatedCurvePoint.setId(1);

        when(curvePointRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockExistedCurvePoint));

        when(curvePointRepository.save(Mockito.any(CurvePoint.class))).thenReturn(mockUpdatedCurvePoint);

        // then
        CurvePoint updatedCurvePoint = cut.updateCurvePoint(mockUpdatedCurvePoint);

        assertEquals(updatedCurvePoint, mockUpdatedCurvePoint);

        verify(curvePointRepository, times(1)).save(Mockito.any(CurvePoint.class));
        verify(curvePointRepository, times(1)).findById(anyInt());

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.curve-point.update", spyCaptor.getValue());
    }

    @Test
    public void updateCurvePointTest_whenCurvePointNoExisted_thenUpdateCurvePoint() {
        // when
        CurvePoint mockNotExistedCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        CurvePoint mockUpdatedCurvePoint = new CurvePoint(1, 13.0d, 1.0d);
        mockNotExistedCurvePoint.setId(1);
        mockUpdatedCurvePoint.setId(1);

        when(curvePointRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.updateCurvePoint(mockUpdatedCurvePoint);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.curve-point.not-found", spyCaptor.getValue());
    }

    @Test
    public void deleteCurvePointTest_whenCurvePointNull_thenThrowCurvePointException() {
        Assertions.assertThatThrownBy(() -> {
            cut.deleteCurvePoint(null);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteCurvePointTest_whenCurvePointIdZero_thenThrowCurvePointException() {

        // when
        CurvePoint mockNotExistedCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        mockNotExistedCurvePoint.setId(0);

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteCurvePoint(mockNotExistedCurvePoint);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteCurvePointTest_whenCurvePointExisted_thenDeleteCurvePoint() {
        // when
        CurvePoint mockExistedCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        mockExistedCurvePoint.setId(1);

        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedCurvePoint));

        // then
        cut.deleteCurvePoint(mockExistedCurvePoint);
        verify(curvePointRepository, times(1)).delete(Mockito.any(CurvePoint.class));
    }

    @Test
    public void deleteCurvePointTest_whenCurvePointNotExisted_thenDeleteCurvePoint() {
        // when
        CurvePoint mockNotExistedCurvePoint = new CurvePoint(1, 14.0d, 10.0d);
        mockNotExistedCurvePoint.setId(1);

        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteCurvePoint(mockNotExistedCurvePoint);
        }).isInstanceOf(CurvePointNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.curve-point.not-found", spyCaptor.getValue());
    }

   
}
