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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;

@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTest {
    
    @Mock
    private RatingRepository ratingRepository;

    @Spy
    private MessageSource messageSource;

    @InjectMocks
    private RatingService cut;

    @Test
    public void findByIdRating_whenRatingNotExisted_thenThrowRatingNotFoundException() {
        //Given

        when(ratingRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        //When
        Assertions.assertThatThrownBy(() -> {
            cut.findById(1);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rating.not-found", spyCaptor.getValue());

    }

    @Test
    public void findByIdRating_whenRatingExisted_thenReturnRating() {
        // when
        Rating mockRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        mockRating.setId(1);
        when(ratingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockRating));

        // then
        Rating findExistingRating = cut.findById(1);

        assertEquals(findExistingRating, mockRating);
    }
    
    @Test
    public void saveRatingTest_whenRatingNull_thenThrowException() {
        Assertions.assertThatThrownBy(() -> {
            cut.saveRating(null);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void saveRatingTest_whenRatingNotNull_thenSaveIt() {
        // when
        Rating mockRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        Rating mockSavedRating = mockRating;
        when(ratingRepository.save(Mockito.any(Rating.class))).thenReturn(mockSavedRating);

        // then
        Rating savedRating = cut.saveRating(mockRating);

        assertEquals(savedRating, mockSavedRating);
        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rating.creation", spyCaptor.getValue());
    }

    @Test
    public void updateRatingTest_whenRatingNull_thenThrowRatingException() {
        Assertions.assertThatThrownBy(() -> {
            cut.updateRating(null);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void updateRatingTest_whenRatingIdNotSame_thenThrowRatingException() {

        // when
        Rating mockExistedRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        Rating mockRatingToUpdate = new Rating("moodysRating2", "sandPrating2", "fitchRating2", 2);
        mockExistedRating.setId(1);
        mockRatingToUpdate.setId(2);

        when(ratingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockExistedRating));

        Assertions.assertThatThrownBy(() -> {
            cut.updateRating(mockRatingToUpdate);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rating.not-found", spyCaptor.getValue());
    }

    @Test
    public void updateRatingTest_whenRatingExisted_thenUpdateRating() {
        // when
        Rating mockExistedRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        Rating mockRatingToUpdate = new Rating("moodysRating2", "sandPrating2", "fitchRating2", 2);
        mockExistedRating.setId(1);
        mockRatingToUpdate.setId(1);

        when(ratingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockExistedRating));

        when(ratingRepository.save(Mockito.any(Rating.class))).thenReturn(mockRatingToUpdate);

        // then
        Rating updatedRating = cut.updateRating(mockRatingToUpdate);

        assertEquals(updatedRating, mockRatingToUpdate);

        verify(ratingRepository, times(1)).save(Mockito.any(Rating.class));
        verify(ratingRepository, times(1)).findById(anyInt());

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rating.update", spyCaptor.getValue());
    }

    @Test
    public void updateRatingTest_whenRatingNoExisted_thenUpdateRating() {
        // when
        Rating mockNotExistedRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        Rating mockRatingToUpdate =new Rating("moodysRating2", "sandPrating2", "fitchRating2", 2);
        mockNotExistedRating.setId(1);
        mockRatingToUpdate.setId(1);

        when(ratingRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.updateRating(mockRatingToUpdate);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rating.not-found", spyCaptor.getValue());
    }

    @Test
    public void deleteRatingTest_whenRatingNull_thenThrowRatingException() {
        Assertions.assertThatThrownBy(() -> {
            cut.deleteRating(null);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteRatingTest_whenRatingIdZero_thenThrowRatingException() {

        // when
        Rating mockNotExistedRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        mockNotExistedRating.setId(0);

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteRating(mockNotExistedRating);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteRatingTest_whenRatingExisted_thenDeleteRating() {
        // when
        Rating mockExistedRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        mockExistedRating.setId(1);

        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedRating));

        // then
        cut.deleteRating(mockExistedRating);
        verify(ratingRepository, times(1)).delete(Mockito.any(Rating.class));
    }

    @Test
    public void deleteRatingTest_whenRatingNotExisted_thenDeleteRating() {
        // when
        Rating mockNotExistedRating = new Rating("moodysRating", "sandPrating", "fitchRating", 1);
        mockNotExistedRating.setId(1);

        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteRating(mockNotExistedRating);
        }).isInstanceOf(RatingNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.rating.not-found", spyCaptor.getValue());
    }
}
