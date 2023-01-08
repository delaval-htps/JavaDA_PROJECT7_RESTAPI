package com.nnk.springboot.security;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
/**
 * Service to add logique after a succesfull authentication with OAuth2:
 * if user exist then we update it with client provider and its id provider.
 * Else with create a new user and save it with infirmations given from
 * Oauth2authentication
 * Use of SavedRequestAwareAuthenticationSuccessHandler and not
 * SimpleUrlAuthenticationSuccessHandler with this implementation springboot
 * save the first url just before login and redirect to it
 */
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Optional<User> existingUser = userService.findByUsername(oAuth2User.getEmail());

        if (existingUser.isPresent()) {
            User existedUser = existingUser.get();
            // update of user with providerId and authenticationProvider if not already done
            log.info(messageSource.getMessage("global.existing-user.oauth2-authenticated",
                    new Object[] { existingUser }, LocaleContextHolder.getLocale()));

            if (existedUser.getAuthenticationProvider() == AuthProvider.LOCAL) {

                userService.updateUserFromOAuth2Authentication(oAuth2User, existedUser);

            } else if ((!Objects.equals(existedUser.getIdProvider(), oAuth2User.getproviderId())
                    || existedUser.getAuthenticationProvider() != oAuth2User.getClientProvider())) {

                throw new OAuth2AuthenticationException("a problem occured with Oauth2Authentication!");
            }

        } else {

            // creation of new user
            log.info(messageSource.getMessage("global.not-existing-user.oauth2-authenticated",
                    new Object[] { "createdUser" }, LocaleContextHolder.getLocale()));
            userService.saveUserFromOAuth2Authentication(oAuth2User);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
