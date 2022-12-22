package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for Rating
 */
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
