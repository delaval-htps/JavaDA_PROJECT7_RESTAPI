package com.nnk.springboot.security;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nnk.springboot.services.UserService;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
public class OAuht2LoginSucessHandlerTest {
    
    @Mock
    private UserService userService;

    @Spy
  private MessageSource messageSource;

    private Authentication mockAuthentication;

    private MockHttpServletRequest mockRequest;

    private MockHttpServletResponse mockResponse;

    @InjectMocks
    @Resource
    private OAuth2LoginSuccessHandler cut = new OAuth2LoginSuccessHandler();;

    @Before
    public void init() {
       
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        mockAuthentication = Mockito.mock(Authentication.class);

    }

    @Test
    public void onAuthenticationSuccess_whenUserNotExisting_SaveUser() throws IOException, ServletException {

        
        Set<GrantedAuthority> mappedGrantedAuthorities = new HashSet<>();
        mappedGrantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("email", "mockemail@gmail.com");
        attributes.put("username", "usernamTest");
        attributes.put("fullname", "fullnameTest");
        attributes.put("clientProvider", AuthProvider.GITHUB);
        attributes.put("providerId", 123);
        
        when(userService.findByUsername(Mockito.anyString())).thenReturn(null);

        
        when(mockAuthentication.getPrincipal()).thenReturn(new CustomOAuth2User(new DefaultOAuth2User(mappedGrantedAuthorities, attributes, "username"),mappedGrantedAuthorities,AuthProvider.GITHUB.toString(),"userNameTest"));
      
        cut.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);
        verify(userService,times(1)).saveUserFromOAuth2Authentication(Mockito.any(CustomOAuth2User.class));
    }

    
}
