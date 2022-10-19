package com.nnk.springboot.services;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotfoundException;
import com.nnk.springboot.repositories.CurvePointRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    @Autowired
    private MessageSource messageSource;

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findById(int id) {
        Optional<CurvePoint> existedCurvePoint = curvePointRepository.findById(id);

        if (existedCurvePoint.isPresent()) {
            log.info(messageSource.getMessage("global.curve-point.find-by-id", new Object[] { id, existedCurvePoint }, new Locale("fr")));
            return existedCurvePoint.get();

        } else {

            throw new CurvePointNotfoundException(messageSource.getMessage("global.curve-point.not-found", new Object[] { id }, new Locale("fr")));
        }
    }

    public CurvePoint saveCurvePoint(CurvePoint curvePoint) {
        if (curvePoint != null) {

            CurvePoint savedCurvePoint = curvePointRepository.save(curvePoint);
            log.info(messageSource.getMessage("global.curve-point.creation", new Object[] { savedCurvePoint }, new Locale("fr")));
            return curvePointRepository.save(curvePoint);

        } else {
            throw new CurvePointNotfoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "curvePoint" }, new Locale("fr")));
        }
    }

    public CurvePoint updateCurvePoint(CurvePoint curvePoint) {
        if (curvePoint != null && curvePoint.getId() != 0) {

            Optional<CurvePoint> existedCurvePoint = curvePointRepository.findById(curvePoint.getId());

            if (existedCurvePoint.isPresent() && Objects.equals(curvePoint.getId(), existedCurvePoint.get().getId())) {

                CurvePoint savedCurvePoint = curvePointRepository.save(curvePoint);
                log.info(messageSource.getMessage("global.curve-point.update", new Object[] { curvePoint }, new Locale("fr")));
                return savedCurvePoint;

            } else {
                throw new CurvePointNotfoundException(messageSource.getMessage("global.curve-point.not-found", new Object[] { curvePoint.getId() }, new Locale("fr")));
            }

        } else {
            throw new CurvePointNotfoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "curvePoint" }, new Locale("fr")));
        }

    }

    public void deleteCurvePoint(CurvePoint curvePoint) {
        if (curvePoint != null && curvePoint.getId() != 0) {

            Optional<CurvePoint> existedCurvePoint = curvePointRepository.findById(curvePoint.getId());

            if (existedCurvePoint.isPresent()) {

                log.info(messageSource.getMessage("global.curve-point.delete", new Object[] { existedCurvePoint }, new Locale("fr")));
                curvePointRepository.delete(existedCurvePoint.get());
                
            } else {
                throw new CurvePointNotfoundException(messageSource.getMessage("global.curve-point.not-found", new Object[] { curvePoint.getId() }, new Locale("fr")));
            }

        } else {
            throw new CurvePointNotfoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "CurvePoint" }, new Locale("fr")));
        }
    }
}
