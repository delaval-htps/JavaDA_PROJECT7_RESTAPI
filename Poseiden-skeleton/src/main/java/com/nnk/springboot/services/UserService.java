package com.nnk.springboot.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {

        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            log.info(messageSource.getMessage("global.user.find-by-id", new Object[] { existingUser.get() }, LocaleContextHolder.getLocale()));
            return existingUser.get();
        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.user.not-found", new Object[] { id }, LocaleContextHolder.getLocale()));
        }
    }

    public User saveUser(User user) {
        if (user != null) {
            User savedUser = userRepository.save(user);
            log.info(messageSource.getMessage("global.user.creation", new Object[] { savedUser }, LocaleContextHolder.getLocale()));
            return savedUser;

        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "user" }, LocaleContextHolder.getLocale()));
        }
    }
    
    public User updateUser(User user) {

        if (user != null && user.getId() != 0) {

            Optional<User> existingUser = userRepository.findById(user.getId());

            if (existingUser.isPresent() && Objects.equals(user.getId(), existingUser.get().getId())) {

                User updatedUser = userRepository.save(user);

                log.info(messageSource.getMessage("global.user.update", new Object[] { updatedUser }, LocaleContextHolder.getLocale()));

                return updatedUser;

            } else {
                throw new UserNotFoundException(messageSource.getMessage("global.user.not-found", new Object[] { user.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "user" }, LocaleContextHolder.getLocale()));
        }

    }

    public void deleteUser(User user) {
        if (user != null && user.getId() != 0) {
 
            Optional<User> existedUser = userRepository.findById(user.getId());
 
            if (existedUser.isPresent()) {
 
                log.info(messageSource.getMessage("global.user.delete", new Object[] { existedUser }, LocaleContextHolder.getLocale()));
 
                userRepository.delete(existedUser.get());
 
            } else {
                throw new UserNotFoundException(messageSource.getMessage("global.user.not-found", new Object[] { user.getId() }, LocaleContextHolder.getLocale()));
            }
 
        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "User" }, LocaleContextHolder.getLocale()));
        }
    }
}
