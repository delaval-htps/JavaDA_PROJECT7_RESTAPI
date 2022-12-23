package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for Trade
 */
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
