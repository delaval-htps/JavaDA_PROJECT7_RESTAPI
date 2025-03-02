package com.nnk.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nnk.springboot.domain.RuleName;
/**
 * Repository for Rulename
 */
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {}
