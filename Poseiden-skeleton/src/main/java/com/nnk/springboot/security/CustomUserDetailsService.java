package com.nnk.springboot.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nnk.springboot.repositories.UserRepository;

/**
 * Service to verify authentication with a username password.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<com.nnk.springboot.domain.User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            List<SimpleGrantedAuthority> userRoles = new ArrayList<>();
            userRoles.add(new SimpleGrantedAuthority(user.get().getRole()));
            return new User(user.get().getUsername(), user.get().getPassword(), userRoles);
        } else {
            throw new UsernameNotFoundException(messageSource.getMessage("global.user.not-found", new Object[] { username }, LocaleContextHolder.getLocale()));
        }
    }

}
