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
    @Autowired
    private TradeService tradeService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/trade/list")
    public String home(Model model) {
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
        if (!result.hasErrors()) {
            tradeService.saveTrade(trade);
            return "redirect:/trade/list";
        } else {
            return "trade/add";
        }
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
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
