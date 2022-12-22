package com.nnk.springboot.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service class for {@link CurvePoint}
 */
@Service
@Log4j2
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * return all existing CurvePoint
     */
    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    /**
     * return curve point with the given id in param
     * 
     * @param id id of the researched curvepoint
     * @return existing curve point with the given id if it exists
     * @throws CurvePointNotFoundException if it doesn't exist
     */
    public CurvePoint findById(int id) {
        Optional<CurvePoint> existedCurvePoint = curvePointRepository.findById(id);

        if (existedCurvePoint.isPresent()) {
            log.info(messageSource.getMessage("global.curve-point.find-by-id",
                    new Object[] { id, existedCurvePoint.get() }, LocaleContextHolder.getLocale()));
            return existedCurvePoint.get();

        } else {

            throw new CurvePointNotFoundException(messageSource.getMessage("global.curve-point.not-found",
                    new Object[] { id }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * save a curvepoint registred from form (cf controller)
     * 
     * @param curvePoint to save
     * @return CurvePoint saved if given curve point not null
     * @throws CurvePointNotFoundException if given curved is null
     */
    public CurvePoint saveCurvePoint(CurvePoint curvePoint) {
        if (curvePoint != null) {

            CurvePoint savedCurvePoint = curvePointRepository.save(curvePoint);
            log.info(messageSource.getMessage("global.curve-point.creation", new Object[] { savedCurvePoint },
                    LocaleContextHolder.getLocale()));
            return curvePointRepository.save(curvePoint);

        } else {
            throw new CurvePointNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "curvePoint" }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Update a existing curve point.
     * 
     * @param curvePoint given in param
     * @return Curve point updated if the given curve point not null
     * @throws CurvePointNotFoundException if given curvepoint is null or it id =0
     *                                     or not found in db
     */
    public CurvePoint updateCurvePoint(CurvePoint curvePoint) {
        if (curvePoint != null && curvePoint.getId() != 0) {

            Optional<CurvePoint> existedCurvePoint = curvePointRepository.findById(curvePoint.getId());

            if (existedCurvePoint.isPresent() && Objects.equals(curvePoint.getId(), existedCurvePoint.get().getId())) {

                CurvePoint savedCurvePoint = curvePointRepository.save(curvePoint);
                log.info(messageSource.getMessage("global.curve-point.update", new Object[] { curvePoint },
                        LocaleContextHolder.getLocale()));
                return savedCurvePoint;

            } else {
                throw new CurvePointNotFoundException(messageSource.getMessage("global.curve-point.not-found",
                        new Object[] { curvePoint.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new CurvePointNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "curvePoint" }, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * delete a given CurvePoint.
     * 
     * @param curvePoint given curve point
     * @throws CurvePointNotFoundException if given curve point is not found in db
     *                                     or null or its id =0
     */
    public void deleteCurvePoint(CurvePoint curvePoint) {
        if (curvePoint != null && curvePoint.getId() != 0) {

            Optional<CurvePoint> existedCurvePoint = curvePointRepository.findById(curvePoint.getId());

            if (existedCurvePoint.isPresent()) {

                log.info(messageSource.getMessage("global.curve-point.delete", new Object[] { existedCurvePoint },
                        LocaleContextHolder.getLocale()));
                curvePointRepository.delete(existedCurvePoint.get());

            } else {
                throw new CurvePointNotFoundException(messageSource.getMessage("global.curve-point.not-found",
                        new Object[] { curvePoint.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new CurvePointNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "CurvePoint" }, LocaleContextHolder.getLocale()));
        }
    }
}
