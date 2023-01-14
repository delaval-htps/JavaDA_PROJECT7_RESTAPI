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

/**
 * Service class for {@link User}
 */
@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * return all user of application.
     * 
     * @return list of user
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * retrieve a existing user with given id.
     * 
     * @param id the given id of researched user
     * @return researching user if existing
     * @throws UserNotFoundException if not existing
     */
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

    /**
     * retrieve a user with given username. use specially when authentication is
     * verify : if it is usernamepassword authentication then username is the
     * username of user .
     * Else if it is OAuth2 authencation then username is the email of Oauth2user.
     * 
     * @param username
     * @return the Optional of existing user 
     */
    public Optional<User> findByUsername(String username) {

        if (!StringUtils.hasText(username.trim())) {
            //internal error
            throw new UserNotFoundException(messageSource.getMessage("global.exception.not-found",
                    new Object[] { "user with username:" + username }, LocaleContextHolder.getLocale()));
        }
        return  userRepository.findByUsername(username);
    }

    /**
     * save a new user
     * 
     * @param user user given in param
     * @return The new saved user
     * @throws UserNotFoundException if given user is null
     */
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

    /**
     * update a user given in param
     * 
     * @param user the user with updated fields to update
     * @return the updated user
     * @throws UserNotFoundException if given user is null or id = 0 or not existing
     */
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

    /**
     * update a existing user with informations retrieve from his oauht2login
     * 
     * @param oAuth2User   the customOauth2user authenticated from Oauth2Login
     * @param existingUser the existingUser corresponding to oauth2user
     * @return updated user with filled fields clientprovider and id provider
     * @throws UserNotFoundException if existingUser or Oauth2user is null
     */
    public User updateUserFromOAuth2Authentication(CustomOAuth2User oAuth2User, User existingUser) {

        if (existingUser != null && oAuth2User != null) {

            existingUser.setAuthenticationProvider(oAuth2User.getClientProvider());
            existingUser.setIdProvider(oAuth2User.getproviderId());
            return userRepository.save(existingUser);

        } else {
            throw new UserNotFoundException(
                    messageSource.getMessage("global.exception.not-null",
                            new Object[] { " update user to save from Oauth2 authentication " },
                            LocaleContextHolder.getLocale()));
        }
    }

    /**
     * delete the given user in param
     * 
     * @param user given user to delete
     * @throws UserNotFoundException if usr is null or its id = 0
     */
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
