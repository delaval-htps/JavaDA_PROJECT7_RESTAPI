package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Bidlist
 */
public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
