package com.nnk.springboot.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Autowired
    private MessageSource messageSource;
    
     /**
     * Retrieve all rating in db.
     *
     * @return List of all registred RuleName(empty if there were not)
     */
     public List<RuleName> findAll() {
         return ruleNameRepository.findAll();
     }
    
     public RuleName findById(Integer id) {
         Optional<RuleName> existingRuleName = ruleNameRepository.findById(id);
         if (existingRuleName.isPresent()) {
             log.info(messageSource.getMessage("global.rule-name.find-by-id", new Object[] { id, existingRuleName.get() }, LocaleContextHolder.getLocale()));
             return existingRuleName.get();
         } else {
             throw new RuleNameNotFoundException(messageSource.getMessage("global.rule-name.not-found", new Object[] { id }, LocaleContextHolder.getLocale()));
         }
     }

     public RuleName saveRuleName(RuleName ruleName) {
         if (ruleName != null) {
             RuleName savedRuleName = ruleNameRepository.save(ruleName);
             log.info(messageSource.getMessage("global.rule-name.creation", new Object[] { savedRuleName }, LocaleContextHolder.getLocale()));
             return savedRuleName;

         } else {
             throw new RuleNameNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "ruleName" }, LocaleContextHolder.getLocale()));
         }
     }
    
     public RuleName updateRuleName(RuleName ruleName) {

         if (ruleName != null && ruleName.getId() != 0) {

             Optional<RuleName> existingRuleName = ruleNameRepository.findById(ruleName.getId());

             if (existingRuleName.isPresent() && Objects.equals(ruleName.getId(), existingRuleName.get().getId())) {

                 RuleName updatedRuleName = ruleNameRepository.save(ruleName);

                 log.info(messageSource.getMessage("global.rule-name.update", new Object[] { updatedRuleName }, LocaleContextHolder.getLocale()));

                 return updatedRuleName;

             } else {
                 throw new RuleNameNotFoundException(messageSource.getMessage("global.rule-name.not-found", new Object[] { ruleName.getId() }, LocaleContextHolder.getLocale()));
             }

         } else {
             throw new RuleNameNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "ruleName" }, LocaleContextHolder.getLocale()));
         }

     }
    
     public void deleteRuleName(RuleName ruleName) {
        if (ruleName != null && ruleName.getId() != 0) {

            Optional<RuleName> existedRuleName = ruleNameRepository.findById(ruleName.getId());

            if (existedRuleName.isPresent()) {

                log.info(messageSource.getMessage("global.rule-name.delete", new Object[] { existedRuleName }, LocaleContextHolder.getLocale()));

                ruleNameRepository.delete(existedRuleName.get());

            } else {
                throw new RuleNameNotFoundException(messageSource.getMessage("global.rule-name.not-found", new Object[] { ruleName.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new RuleNameNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "RuleName" }, LocaleContextHolder.getLocale()));
        }
    }


}
