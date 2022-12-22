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

/**
 * Controller for RuleName entity
 */
@Controller
public class RuleNameController {
    @Autowired
    private RuleNameService ruleNameService;

    @Autowired
    private MessageSource messageSource;

    /**
     * endpoint to show the list of all existing rule name
     * 
     * @param model
     * @return the view of list of all rule name
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleName> rules = ruleNameService.findAll();
        model.addAttribute("listOfRuleName", rules);
        return "ruleName/list";
    }

    /**
     * endpoint to show form to save a new rule name.
     * 
     * @param curve the curve to save
     * @return the view with the form to save a new rule name
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    /**
     * endoint to save a new rule name.
     * 
     * @param curvePoint the rule name retrieved from form
     * @param result     bindignresult if error in filled fields
     * @param model
     * @return the view of updated list of rule name or the view of form to save
     *         rule name if there is a error in field
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ruleNameService.saveRuleName(ruleName);
            return "redirect:/ruleName/list";
        } else {
            return "ruleName/add";
        }
    }

    /**
     * endpoint to show the form to update a existing rule name selected from the
     * list
     * of them.
     * 
     * @param id    the id od rule name to update
     * @param model
     * @return the view of form to update the rule name with given id.
     * @throws GlobalPoseidonException if the given id is not correct ( equal to
     *                                 zero)
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {

            RuleName existingRuleName = ruleNameService.findById(id);
            model.addAttribute("ruleName", existingRuleName);
            return "ruleName/update";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to update the rule name with given id before .
     * 
     * @param id      the given id of rule name to update
     * @param bidList the rule name to save with updated fields from the update
     *                form
     * @param result  bindingResult if there is a error of validation in fields
     * @param model
     * @return the view of updated list of rule name if update was done correctly
     *         or again
     *         view of update form to show which fields are not valid with error
     *         message
     * @Throws {@link GlobalPoseidonException} if given id is not correct (=0)
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName, BindingResult result,
            Model model) {
        if (id != 0) {

            if (!result.hasErrors()) {
                ruleNameService.updateRuleName(ruleName);
                return "redirect:/ruleName/list";

            } else {
                return ("ruleName/update");
            }

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to delete rule name with given id
     * 
     * @param id    the given id of rule name to delete
     * @param model
     * @return the updated list of rule name with deletion done
     * @throws GlobalPoseidonException if the given id is not correct (=0)
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {

            RuleName existingRuleName = ruleNameService.findById(id);
            ruleNameService.deleteRuleName(existingRuleName);
            return "redirect:/ruleName/list";

        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
