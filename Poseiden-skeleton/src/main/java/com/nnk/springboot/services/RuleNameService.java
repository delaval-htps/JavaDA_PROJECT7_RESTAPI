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

/**
 * Service class for {@link RuleName}
 */
@Service
@Log4j2
public class RuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Retrieve all rulename in db.
     *
     * @return List of all registred RuleName(empty if there were not)
     */
    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    /**
     * find a rule name with a given id in db.
     * 
     * @param id the given id of researched rule name
     * @return Existing rule name if it exists.
     * @throws RuleNameNotFoundException if rulename not existing in db
     */
    public RuleName findById(Integer id) {
        Optional<RuleName> existingRuleName = ruleNameRepository.findById(id);
        if (existingRuleName.isPresent()) {
            log.info(messageSource.getMessage("global.rule-name.find-by-id",
                    new Object[] { id, existingRuleName.get() }, LocaleContextHolder.getLocale()));
            return existingRuleName.get();
        } else {
            throw new RuleNameNotFoundException(messageSource.getMessage("global.rule-name.not-found",
                    new Object[] { id }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * save a new Rulename.
     * 
     * @param ruleName the given rulename to save.
     * @return Saved rulename.
     * @throws RuleNameNotFoundException if given rulename is null.
     */
    public RuleName saveRuleName(RuleName ruleName) {
        if (ruleName != null) {
            RuleName savedRuleName = ruleNameRepository.save(ruleName);
            log.info(messageSource.getMessage("global.rule-name.creation", new Object[] { savedRuleName },
                    LocaleContextHolder.getLocale()));
            return savedRuleName;

        } else {
            throw new RuleNameNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "ruleName" }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * update rulename.
     * 
     * @param ruleName the given rulename to update.
     * @return updated rulename
     * @throws RuleNameNotFoundException if given rulename is null or id =0
     */
    public RuleName updateRuleName(RuleName ruleName) {

        if (ruleName != null && ruleName.getId() != 0) {

            Optional<RuleName> existingRuleName = ruleNameRepository.findById(ruleName.getId());

            if (existingRuleName.isPresent() && Objects.equals(ruleName.getId(), existingRuleName.get().getId())) {

                RuleName updatedRuleName = ruleNameRepository.save(ruleName);

                log.info(messageSource.getMessage("global.rule-name.update", new Object[] { updatedRuleName },
                        LocaleContextHolder.getLocale()));

                return updatedRuleName;

            } else {
                throw new RuleNameNotFoundException(messageSource.getMessage("global.rule-name.not-found",
                        new Object[] { ruleName.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new RuleNameNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "ruleName" }, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * delete rulename.
     * 
     * @param ruleName the given rulename to delete
     */
    public void deleteRuleName(RuleName ruleName) {
        if (ruleName != null && ruleName.getId() != 0) {

            Optional<RuleName> existedRuleName = ruleNameRepository.findById(ruleName.getId());

            if (existedRuleName.isPresent()) {

                log.info(messageSource.getMessage("global.rule-name.delete", new Object[] { existedRuleName },
                        LocaleContextHolder.getLocale()));

                ruleNameRepository.delete(existedRuleName.get());

            } else {
                throw new RuleNameNotFoundException(messageSource.getMessage("global.rule-name.not-found",
                        new Object[] { ruleName.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new RuleNameNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "RuleName" }, LocaleContextHolder.getLocale()));
        }
    }

}
