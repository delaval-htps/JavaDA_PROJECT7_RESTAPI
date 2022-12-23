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

/**
 * Controller for CurvePoint entity
 */
@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    @Autowired
    private MessageSource messageSource;

    /**
     * endpoint to show the list of all existing curve point
     * 
     * @param model
     * @return the view of list of all curve point
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePoint> curvePoints = curvePointService.findAll();
        model.addAttribute("listOfCurvePoint", curvePoints);
        return "curvePoint/list";
    }

    /**
     * endpoint to show form to save a nex curve point.
     * 
     * @param curve the curve to save
     * @return the view with the form to save a new curve point
     */
    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint curve) {
        return "curvePoint/add";
    }

    /**
     * endoint to save a new curve point.
     * 
     * @param curvePoint the curve point retrieved from form
     * @param result     bindignresult if error in filled fields
     * @param model
     * @return the view of updated list of curve point or the view of form to save
     *         curve point if there is a error in field
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {

        if (!result.hasErrors()) {

            curvePointService.saveCurvePoint(curvePoint);
            return "redirect:/curvePoint/list";

        } else {

            return "curvePoint/add";
        }

    }

    /**
     * endpoint to show the form to update a existing curvepoint selected from the
     * list
     * of them.
     * 
     * @param id    the id od curvepoint to update
     * @param model
     * @return the view of form to update the curvepoint with given id.
     * @throws GlobalPoseidonException if the given id is not correct ( equal to
     *                                 zero)
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        if (id != 0) {

            CurvePoint existingCurvePoint = curvePointService.findById(id);
            model.addAttribute("curvePoint", existingCurvePoint);
            return "curvePoint/update";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * endpoint to update the curve point with given id before .
     * 
     * @param id      the given id of curve point to update
     * @param curvepoint the curve point to save with updated fields from the update
     *                form
     * @param result  bindingResult if there is a error of validation in fields
     * @param model
     * @return the view of updated list of curve point if update was done correctly
     *         or again
     *         view of update form to show which fields are not valid with error
     *         message
     * @Throws {@link GlobalPoseidonException} if given id is not correct (=0)
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint, BindingResult result,
            Model model) {
        if (id != 0) {

            if (!result.hasErrors()) {

                curvePointService.updateCurvePoint(curvePoint);
                return "redirect:/curvePoint/list";

            } else {

                return ("curvePoint/update");
            }

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to delete curvepoint with given id
     * 
     * @param id    the given id of curvepoint to delete
     * @param model
     * @return the updated list of curvepoint with deletion done
     * @throws GlobalPoseidonException if the given id is not correct (=0)
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {

            CurvePoint existingCurvePoint = curvePointService.findById(id);
            curvePointService.deleteCurvePoint(existingCurvePoint);
            return "redirect:/curvePoint/list";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
