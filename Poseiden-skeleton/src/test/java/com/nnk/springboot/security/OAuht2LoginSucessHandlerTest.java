package com.nnk.springboot.security;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nnk.springboot.services.UserService;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@Profile("test")
public class OAuht2LoginSucessHandlerTest {

    @Mock
    private UserService userService;

    @Spy
    private MessageSource messageSource;

    private Authentication mockAuthentication;

    private MockHttpServletRequest mockRequest;

    private MockHttpServletResponse mockResponse;

    private CustomOAuth2User customOAuth2User;

    @InjectMocks
    private OAuth2LoginSuccessHandler cut = new OAuth2LoginSuccessHandler();;

    @Before
    public void init() {

        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        mockAuthentication = Mockito.mock(Authentication.class);

        Set<GrantedAuthority> mappedGrantedAuthorities = new HashSet<>();
        mappedGrantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("email", "mockemail@gmail.com");
        attributes.put("login", "user");
        attributes.put("name", "fullnameTest");
        attributes.put("clientProvider", AuthProvider.GITHUB);
        attributes.put("id", 123);
        customOAuth2User = new CustomOAuth2User(
                new DefaultOAuth2User(mappedGrantedAuthorities, attributes, "login"),
                mappedGrantedAuthorities, AuthProvider.GITHUB.toString(), "user");
    }

    @Test
    public void onAuthenticationSuccess_whenUserNotExisting_ThenThrowOauth2AuthenticationException() throws IOException, ServletException {

      
        when(mockAuthentication.getPrincipal()).thenReturn(
            customOAuth2User);

        when(userService.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(OAuth2AuthenticationException.class,()->{cut.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);} );
        
        verify(userService,never()).updateUserFromOAuth2Authentication(Mockito.any(CustomOAuth2User.class),Mockito.any(com.nnk.springboot.domain.User.class));
    }

    @Test
    public void onAuthenticationSuccess_whenUserExistingWithAuthProviderLOCAL_ThenUpdateUser() throws IOException, ServletException {
       
        when(mockAuthentication.getPrincipal()).thenReturn(customOAuth2User);
        com.nnk.springboot.domain.User existingUser = new com.nnk.springboot.domain.User("user", "email@gmail.com",
                "fullnameTest", "USER", AuthProvider.LOCAL, 123);

        when(userService.findByUsername(Mockito.anyString())).thenReturn(Optional.of(existingUser));

        cut.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);
        
        //verify(userService, never()).saveUserFromOAuth2Authentication(Mockito.any(CustomOAuth2User.class));
        verify(userService, times(1)).updateUserFromOAuth2Authentication(Mockito.any(CustomOAuth2User.class),Mockito.any(com.nnk.springboot.domain.User.class));
    }

    /**
     * For test below, Oauth2User must have as AuthProvider GITHUB because of CustomOAuth2UserDetailsService that check that
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void onAuthenticationSuccess_whenUserExistingAuthProviderAndIdProviderSameOauth2User_ThenUpdateUser() throws IOException, ServletException {
       
        when(mockAuthentication.getPrincipal()).thenReturn(customOAuth2User);
        com.nnk.springboot.domain.User existingUser = new com.nnk.springboot.domain.User("user", "email@gmail.com",
                "fullnameTest", "USER", AuthProvider.GITHUB, 123);

        when(userService.findByUsername(Mockito.anyString())).thenReturn(Optional.of(existingUser));

        cut.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);
        
       // verify(userService, never()).saveUserFromOAuth2Authentication(Mockito.any(CustomOAuth2User.class));
        verify(userService, never()).updateUserFromOAuth2Authentication(Mockito.any(CustomOAuth2User.class),Mockito.any(com.nnk.springboot.domain.User.class));
    }

    @Test
    public void onAuthenticationSuccess_whenUserExistingAuthProviderNotSameOauth2User_ThenUpdateUser() throws IOException, ServletException {
       
        when(mockAuthentication.getPrincipal()).thenReturn(customOAuth2User);
        com.nnk.springboot.domain.User existingUser = new com.nnk.springboot.domain.User("user", "email@gmail.com",
                "fullnameTest", "USER", AuthProvider.TEST, 123);

        when(userService.findByUsername(Mockito.anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(OAuth2AuthenticationException.class,()->{cut.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);});
    }

    @Test
    public void onAuthenticationSuccess_whenUserExistingProviderIdNotSameOauth2User_ThenUpdateUser() throws IOException, ServletException {
       
        when(mockAuthentication.getPrincipal()).thenReturn(customOAuth2User);
        com.nnk.springboot.domain.User existingUser = new com.nnk.springboot.domain.User("user", "email@gmail.com",
                "fullnameTest", "USER", AuthProvider.GITHUB, 4);

        when(userService.findByUsername(Mockito.anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(OAuth2AuthenticationException.class,()->{cut.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);} );
    }
}
