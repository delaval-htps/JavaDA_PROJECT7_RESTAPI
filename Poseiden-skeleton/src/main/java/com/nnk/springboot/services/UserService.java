package com.nnk.springboot.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.security.CustomOAuth2User;

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
            log.info(messageSource.getMessage("global.user.find-by-id", new Object[] { existingUser.get() },
                    LocaleContextHolder.getLocale()));
            return existingUser.get();
        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.user.not-found", new Object[] { id },
                    LocaleContextHolder.getLocale()));
        }
    }

    public User findByUsername(String username) {

        if (!StringUtils.hasText(username.trim())) {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-found",
                    new Object[] { "user with username:" + username }, LocaleContextHolder.getLocale()));
        }
        User existingUser = userRepository.findByUsername(username);
        log.info(messageSource.getMessage("global.user.find-by-username", new Object[] { username, existingUser },
                LocaleContextHolder.getLocale()));
        return existingUser;
    }

    public User saveUser(User user) {

        if (user != null) {

            User savedUser = userRepository.save(user);
            log.info(messageSource.getMessage("global.user.creation", new Object[] { savedUser },
                    LocaleContextHolder.getLocale()));
            return savedUser;

        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "user" }, LocaleContextHolder.getLocale()));
        }
    }

    public User updateUserFromOAuth2Authentication(CustomOAuth2User oAuth2User, User existingUser) {

        if (existingUser != null && oAuth2User != null) {

            existingUser.setAuthenticationProvider(oAuth2User.getClientProvider());
            existingUser.setIdProvider(oAuth2User.getproviderId());
            return userRepository.save(existingUser);

        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { " update user to save from Oauth2 authentication " },
                    LocaleContextHolder.getLocale()));
        }
    }

    public User saveUserFromOAuth2Authentication(CustomOAuth2User oAuth2User) {

        if (oAuth2User != null) {

            return userRepository.save(new User(oAuth2User.getUsername(), oAuth2User.getEmail(),
                    oAuth2User.getFullname(), "USER", oAuth2User.getClientProvider(), oAuth2User.getproviderId()));

        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { " new user to save from Oauth2 authentication " }, LocaleContextHolder.getLocale()));
        }
    }

    public User updateUser(User user) {

        if (user != null && user.getId() != 0) {

            Optional<User> existingUser = userRepository.findById(user.getId());

            if (existingUser.isPresent() && Objects.equals(user.getId(), existingUser.get().getId())) {

                User updatedUser = userRepository.save(user);

                log.info(messageSource.getMessage("global.user.update", new Object[] { updatedUser },
                        LocaleContextHolder.getLocale()));

                return updatedUser;

            } else {
                throw new UserNotFoundException(messageSource.getMessage("global.user.not-found",
                        new Object[] { user.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "user" }, LocaleContextHolder.getLocale()));
        }

    }

    public void deleteUser(User user) {
        if (user != null && user.getId() != 0) {

            Optional<User> existedUser = userRepository.findById(user.getId());

            if (existedUser.isPresent()) {

                log.info(messageSource.getMessage("global.user.delete", new Object[] { existedUser },
                        LocaleContextHolder.getLocale()));

                userRepository.delete(existedUser.get());

            } else {
                throw new UserNotFoundException(messageSource.getMessage("global.user.not-found",
                        new Object[] { user.getId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-null",
                    new Object[] { "User" }, LocaleContextHolder.getLocale()));
        }
    }

}
