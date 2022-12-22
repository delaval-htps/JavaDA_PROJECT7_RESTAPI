package com.nnk.springboot.security;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@Profile("test")
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private MessageSource messageSource;

    @InjectMocks
    private CustomUserDetailsService cut = new CustomUserDetailsService();

    @Test
    public void loadByUsername_whenNotExistingUser_thenThrowException() {
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, ()->{
            cut.loadUserByUsername("test0");
        });
    }

    @Test
    public void loadByUsername_whenExistingUser_thenReturnNewUser() {
        User mockUser = new User("Username", "email@gmail.com", "fullname", "USER", AuthProvider.GITHUB, 123);
        mockUser.setPassword("test4all&lp");
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        assertTrue(cut.loadUserByUsername("test0") instanceof org.springframework.security.core.userdetails.User);
    }
}
