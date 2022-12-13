package com.nnk.springboot.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.nnk.springboot.domain.User;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    
    @Query(value = "select u from User u where u.username =?1 or u.email=?1")
    Optional<User> findByUsername(String username);

}
