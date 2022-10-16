package com.nnk.springboot.controllers;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.BidListService;

@Controller
public class BidListController {

    @Autowired
    private BidListService bidListService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("listOfBidList", bidListService.findBidLists());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            bidListService.saveBidList(bid);
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form

        if (id != 0) {
            BidList existedBidList = bidListService.findById(id);
            model.addAttribute("bidList", existedBidList);

            return "bidList/update";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, new Locale("fr")));
        }

    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList, BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and
        // return
        // list Bid

        if (id != 0) {

            if (!result.hasErrors()) {

                bidListService.updateBidList(bidList);
                return "redirect:/bidList/list";

            } else {
                return ("bidList/update");
            }

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, new Locale("fr")));
        }
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        if (id != 0) {

            BidList existedBidList = bidListService.findById(id);
            bidListService.deleteBidList(existedBidList);
            return "redirect:/bidList/list";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, new Locale("fr")));
        }
    }
}
