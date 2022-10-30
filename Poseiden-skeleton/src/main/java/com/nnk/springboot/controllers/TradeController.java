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

@Controller
public class TradeController {
    // TODO: Inject Trade service
    @Autowired
    private TradeService tradeService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/trade/list")
    public String home(Model model) {
        // TODO: find all Trade, add to model
        List<Trade> trades = tradeService.findAll();
        model.addAttribute("listOfTrade", trades);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Trade list
        if (!result.hasErrors()) {
            tradeService.saveTrade(trade);
            return "redirect:/trade/list";
        } else {
            return "trade/add";
        }
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Trade by Id and to model then show to the form
        if (id != 0) {

            Trade existingTrade = tradeService.findById(id);
            model.addAttribute("trade", existingTrade);
            return "trade/update";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade, BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Trade
        // and return Trade list
        if (id != 0) {

            if (!result.hasErrors()) {
                tradeService.updateTrade(trade);
                return "redirect:/trade/list";

            } else {
                return ("trade/update");
            }

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Trade by Id and delete the Trade, return to Trade list
        if (id != 0) {

            Trade existingTrade = tradeService.findById(id);
            tradeService.deleteTrade(existingTrade);
            return "redirect:/trade/list";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
