package com.nnk.springboot.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Services class for {@link Rating}
 */
@Service
@Log4j2
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Retrieve all rating in db.
     * 
     * @return List of all registred Rating(empty if there were not)
     */
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    /**
     * Return the rating with given id in param.
     * 
     * @param id the given id of rating
     * @return Rating if existing
     * @throws RatingNotFoundException if rating is not present.
     */
    public Rating findById(Integer id) {
        Optional<Rating> existingRating = ratingRepository.findById(id);
        if (existingRating.isPresent()) {
            log.info(messageSource.getMessage("global.rating.find-by-id", new Object[] { id, existingRating.get() },
                    LocaleContextHolder.getLocale()));
            return existingRating.get();
        } else {
            throw new RatingNotFoundException(messageSource.getMessage("global.rating.not-found", new Object[] { id },
                    LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Save a given rating.
     * 
     * @param rating the given rating to save
     * @return Saved rating
     * @throws RatingNotFoundException if the given rating is null
     */
    public Rating saveRating(Rating rating) {
        if (rating != null) {
            Rating savedRating = ratingRepository.save(rating);
            log.info(messageSource.getMessage("global.rating.creation", new Object[] { savedRating },
                    LocaleContextHolder.getLocale()));
            return savedRating;

        } else {
            throw new RatingNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "rating" }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Update existing Rating.
     * 
     * @param rating the given rating to update
     * @return updated rating if given rating is not null and id !=0
     * @throws RatingNotFoundException if given rating is null or id =0
     */
    public Rating updateRating(Rating rating) {

        if (rating != null && rating.getId() != 0) {

            Optional<Rating> existingRating = ratingRepository.findById(rating.getId());

            if (existingRating.isPresent() && Objects.equals(rating.getId(), existingRating.get().getId())) {

                Rating updatedRating = ratingRepository.save(rating);

                log.info(messageSource.getMessage("global.rating.update", new Object[] { updatedRating },
                        LocaleContextHolder.getLocale()));

                return updatedRating;

            } else {
                throw new RatingNotFoundException(messageSource.getMessage("global.rating.not-found",
                        new Object[] { rating.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new RatingNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "rating" }, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * delete given rating in param.
     * 
     * @param rating rating to delete.
     */
    public void deleteRating(Rating rating) {
        if (rating != null && rating.getId() != 0) {

            Optional<Rating> existedRating = ratingRepository.findById(rating.getId());

            if (existedRating.isPresent()) {

                log.info(messageSource.getMessage("global.rating.delete", new Object[] { existedRating },
                        LocaleContextHolder.getLocale()));

                ratingRepository.delete(existedRating.get());

            } else {
                throw new RatingNotFoundException(messageSource.getMessage("global.rating.not-found",
                        new Object[] { rating.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new RatingNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "Rating" }, LocaleContextHolder.getLocale()));
        }
    }
}
