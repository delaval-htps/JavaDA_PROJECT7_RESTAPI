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

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.TradeService;

/**
 * Controller for Trade entity
 */
@Controller
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @Autowired
    private MessageSource messageSource;

    /**
     * endpoint to show the list of all existing trade
     * 
     * @param model
     * @return the view of list of all trade
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        List<Trade> trades = tradeService.findAll();
        model.addAttribute("listOfTrade", trades);
        return "trade/list";
    }

    /**
     * endpoint to show form to save a new trade.
     * 
     * @param curve the curve to save
     * @return the view with the form to save a new trade
     */
    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    /**
     * endoint to save a new trade.
     * 
     * @param curvePoint the trade retrieved from form
     * @param result     bindignresult if error in filled fields
     * @param model
     * @return the view of updated list of trade or the view of form to save
     *         trade if there is a error in field
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            tradeService.saveTrade(trade);
            return "redirect:/trade/list";
        } else {
            return "trade/add";
        }
    }

    /**
     * endpoint to show the form to update a existing trade selected from the
     * list
     * of them.
     * 
     * @param id    the id od trade to update
     * @param model
     * @return the view of form to update the trade with given id.
     * @throws GlobalPoseidonException if the given id is not correct ( equal to
     *                                 zero)
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {

            Trade existingTrade = tradeService.findById(id);
            model.addAttribute("trade", existingTrade);
            return "trade/update";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to update the trade with given id before .
     * 
     * @param id      the given id of trade to update
     * @param bidList the trade to save with updated fields from the update
     *                form
     * @param result  bindingResult if there is a error of validation in fields
     * @param model
     * @return the view of updated list of trade if update was done correctly
     *         or again
     *         view of update form to show which fields are not valid with error
     *         message
     * @Throws {@link GlobalPoseidonException} if given id is not correct (=0)
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade, BindingResult result, Model model) {
        if (id != 0) {

            if (!result.hasErrors()) {
                tradeService.updateTrade(trade);
                return "redirect:/trade/list";

            } else {
                return ("trade/update");
            }

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to delete trade with given id
     * 
     * @param id    the given id of trade to delete
     * @param model
     * @return the updated list of trade with deletion done
     * @throws GlobalPoseidonException if the given id is not correct (=0)
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {

            Trade existingTrade = tradeService.findById(id);
            tradeService.deleteTrade(existingTrade);
            return "redirect:/trade/list";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
