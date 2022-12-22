package com.nnk.springboot.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

/**
 * Services for authentication with github.
 * Use to overide loadUser and valid authentication with OAuth2
 * If authentication is only from github client provider: we accept it and
 * return a new CustomOauth2user with informations that we need retrieved
 * from existing user in db if it exists( specially its authority (ADMIN or
 * USER).Else with informations from gtihub provider and authority USER.
 */
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // check if provider is only GITHUB
        if (!userRequest.getClientRegistration().getClientName().equalsIgnoreCase("github")) {
            throw new OAuth2AuthenticationException("this registrationProvider is not permitted");
        }

        // fetch the OAuth2User from userRequest
        OAuth2User loadUser = super.loadUser(userRequest);

        // retrieve from DB a potentiel existing registred user with same email of
        // connected OAuth2user
        Optional<User> existingUser = userRepository.findByUsername(loadUser.getAttribute("email"));

        Set<GrantedAuthority> mappedGrantedAuthorities = new HashSet<>();

        if (existingUser.isPresent()) {

            mappedGrantedAuthorities.add(new SimpleGrantedAuthority(existingUser.get().getRole()));

            return new CustomOAuth2User(loadUser, mappedGrantedAuthorities,
                    userRequest.getClientRegistration().getClientName(), existingUser.get().getUsername());

        } else {
            mappedGrantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            return new CustomOAuth2User(loadUser, mappedGrantedAuthorities,
                    userRequest.getClientRegistration().getClientName(), loadUser.getAttribute("username"));
        }
    }

}
