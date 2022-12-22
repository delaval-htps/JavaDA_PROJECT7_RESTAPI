package com.nnk.springboot.controllers;

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

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.GlobalExceptionHandler;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.BidListService;

/**
 * Controller for BidList entity.
 */
@Controller
public class BidListController {

    @Autowired
    private BidListService bidListService;

    @Autowired
    private MessageSource messageSource;

    /**
     * endpoint to show the list of all bidlist
     * 
     * @param model
     * @return view list.html for bidlist
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("listOfBidList", bidListService.findBidLists());
        return "bidList/list";
    }

    /**
     * endpoint to show form for adding bidlist
     * 
     * @param bid bislist to add
     * @return view add.html for bidlist
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     * endpoint to save a new bidlist
     * 
     * @param bid    the new bidlist to save retrive form form
     * @param result the binding result when one or more fields of bidlist is not
     *               valid
     * @param model  model to inject object
     * @return the view to add a new bidlist if it is not valid or view of list of
     *         bidlist if save was correctly done.
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            bidListService.saveBidList(bid);
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    /**
     * endpoint to show the form to update a existing bidlist selected from the list
     * of them.
     * 
     * @param id    the id od bidlist to update
     * @param model
     * @return the view of form to update the bidlist with given id.
     * @throws GlobalPoseidonException if the given id is not correct ( equal to
     *                                 zero)
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        if (id != 0) {
            BidList existingBidList = bidListService.findById(id);
            model.addAttribute("bidList", existingBidList);

            return "bidList/update";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * endpoint to update the bidlist with given id before .
     * 
     * @param id      the given id of bidlist to update
     * @param bidList the bidlist to save with updated fields from the update form
     * @param result  bindingResult if there is a error of validation in fields
     * @param model
     * @return the view of list of bidlist if update was done correctly or again
     *         view of update form to show which fields are not valid with error
     *         message
     * @Throws {@link GlobalPoseidonException} if given id is not correct (=0)
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList, BindingResult result, Model model) {

        if (id != 0) {

            if (!result.hasErrors()) {

                bidListService.updateBidList(bidList);
                return "redirect:/bidList/list";

            } else {
                return ("bidList/update");
            }

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to delete bidlist with given id
     * 
     * @param id    the given id of bidlist to delete
     * @param model
     * @return the updated list of bidlist with deletion done
     * @throws GlobalPoseidonException if the given id is not correct (=0)
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {

            BidList existedBidList = bidListService.findById(id);
            bidListService.deleteBidList(existedBidList);
            return "redirect:/bidList/list";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
