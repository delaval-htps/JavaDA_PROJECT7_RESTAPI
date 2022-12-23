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

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.RatingService;

/**
 * Controller for Rating entity
 */
@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private MessageSource messageSource;

    /**
     * endpoint to show the list of all existing rating
     * 
     * @param model
     * @return the view of list of all rating
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        List<Rating> ratings = ratingService.findAll();
        model.addAttribute("listOfRating", ratings);
        return "rating/list";
    }

    /**
     * endpoint to show form to save a nex rating.
     * 
     * @param rating the rating to save
     * @return the view with the form to save a new rating
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    /**
     * endoint to save a new rating.
     * 
     * @param rating the rating retrieved from form
     * @param result     bindignresult if error in filled fields
     * @param model
     * @return the view of updated list of rating or the view of form to save
     *         rating if there is a error in field
     */

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (!result.hasErrors()) {

            ratingService.saveRating(rating);
            return "redirect:/rating/list";

        } else {

            return "rating/add";
        }

    }

    /**
     * endpoint to show the form to update a existing rating selected from the
     * list
     * of them.
     * 
     * @param id    the id od rating to update
     * @param model
     * @return the view of form to update the rating with given id.
     * @throws GlobalPoseidonException if the given id is not correct ( equal to
     *                                 zero)
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        if (id != 0) {

            Rating existingRating = ratingService.findById(id);
            model.addAttribute("rating", existingRating);
            return "rating/update";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * endpoint to update the rating with given id before .
     * 
     * @param id      the given id of rating to update
     * @param rating the rating to save with updated fields from the update
     *                form
     * @param result  bindingResult if there is a error of validation in fields
     * @param model
     * @return the view of updated list of rating if update was done correctly
     *         or again
     *         view of update form to show which fields are not valid with error
     *         message
     * @Throws {@link GlobalPoseidonException} if given id is not correct (=0)
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating, BindingResult result,
            Model model) {

        if (id != 0) {

            if (!result.hasErrors()) {

                ratingService.updateRating(rating);
                return "redirect:/rating/list";

            } else {

                return ("rating/update");
            }

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to delete rating with given id
     * 
     * @param id    the given id of rating to delete
     * @param model
     * @return the updated list of rating with deletion done
     * @throws GlobalPoseidonException if the given id is not correct (=0)
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {

            Rating existingRating = ratingService.findById(id);
            ratingService.deleteRating(existingRating);
            return "redirect:/rating/list";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
