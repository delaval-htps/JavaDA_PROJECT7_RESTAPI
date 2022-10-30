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

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.RuleNameService;

@Controller
public class RuleNameController {
    // TODO: Inject RuleName service
    @Autowired
    private RuleNameService ruleNameService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleName> rules = ruleNameService.findAll();
        model.addAttribute("listOfRuleName", rules);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return RuleName
        // list
        if (!result.hasErrors()) {
            ruleNameService.saveRuleName(ruleName);
            return "redirect:/ruleName/list";
        } else {
            return "ruleName/add";
        }
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get RuleName by Id and to model then show to the form
        if (id != 0) {

            RuleName existingRuleName = ruleNameService.findById(id);
            model.addAttribute("ruleName", existingRuleName);
            return "ruleName/update";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName, BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update RuleName
        // and return RuleName list
        if (id != 0) {

            if (!result.hasErrors()) {
                ruleNameService.updateRuleName(ruleName);
                return "redirect:/ruleName/list";

            } else {
                return ("ruleName/update");
            }

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        // TODO: Find RuleName by Id and delete the RuleName, return to Rule
        // list
        if (id != 0) {

            RuleName existingRuleName = ruleNameService.findById(id);
            ruleNameService.deleteRuleName(existingRuleName);
            return "redirect:/ruleName/list";

        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
