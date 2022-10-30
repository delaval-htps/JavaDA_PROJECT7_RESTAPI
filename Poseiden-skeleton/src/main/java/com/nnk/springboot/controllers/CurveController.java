package com.nnk.springboot.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.CurvePointService;

@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePoint> curvePoints = curvePointService.findAll();
        model.addAttribute("listOfCurvePoint", curvePoints);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint curve) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {

        if (!result.hasErrors()) {

            curvePointService.saveCurvePoint(curvePoint);
            return "redirect:/curvePoint/list";

        } else {

            return "curvePoint/add";
        }

    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        if (id != 0) {

            CurvePoint existingCurvePoint = curvePointService.findById(id);
            model.addAttribute("curvePoint", existingCurvePoint);
            return "curvePoint/update";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }

    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint, BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Curve
        // and return Curve list
        if (id != 0) {

            if (!result.hasErrors()) {

                curvePointService.updateCurvePoint(curvePoint);
                return "redirect:/curvePoint/list";

            } else {

                return ("curvePoint/update");
            }

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Curve by Id and delete the Curve, return to Curve list
        if (id != 0) {

            CurvePoint existingCurvePoint = curvePointService.findById(id);
            curvePointService.deleteCurvePoint(existingCurvePoint);
            return "redirect:/curvePoint/list";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
