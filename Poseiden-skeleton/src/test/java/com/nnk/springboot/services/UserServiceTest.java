package com.nnk.springboot.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.security.AuthProvider;
import com.nnk.springboot.security.CustomOAuth2User;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private MessageSource messageSource;

    @InjectMocks
    private UserService cut;

    private List<User> users;
    private User mockUser1, mockUser2;

    @Before
    public void intialize() {
        users = new ArrayList<>();
        mockUser1 = new User(1, "username", "email", "password", "fullName", "userRole");
        mockUser2 = new User(2, "username", "email", "password", "fullName", "userRole");
        users.add(mockUser1);
        users.add(mockUser2);
    }

    @Test
    public void findAll() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> findAll = cut.findAll();
        assertEquals(findAll, users);
    }

    @Test
    public void findByIdUser_whenUserNotExisted_thenThrowUserException() {
        // Given

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & then
        Assertions.assertThatThrownBy(() -> {
            cut.findById(1);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.user.not-found", spyCaptor.getValue());
    }

    @Test
    public void findByIdUser_whenUserExisted_thenReturnUser() {
        // Given
        User mockExistedUser = new User(1, "username", "email@email.com", "password", "fullName", "userRole");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedUser));

        // when & then
        User findExistedUser = cut.findById(1);

        assertEquals(findExistedUser, mockExistedUser);
    }

    @Test
    public void findByUsername_whenUserNotExisted_thenReturnOptinoalEmpty() {
        // Given

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When 
            Optional<User> findByUsername = cut.findByUsername("test");

        //Then   
        assertTrue(findByUsername.isEmpty());
    }

    @Test
    public void findByUsername_whenUserExisted_thenReturnUser() {
        // Given
        User mockExistedUser = new User(1, "username", "email@email.com", "password", "fullName", "userRole");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockExistedUser));

        // when & then
        Optional<User> findExistedUser = cut.findByUsername("username");

        assertEquals(findExistedUser.get(), mockExistedUser);
    }

    @Test
    public void findByUsername_UsernameEmpty_thenThrowException() {

        Assertions.assertThatThrownBy(() -> {
            cut.findByUsername("");
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-found", spyCaptor.getValue());
    }

    @Test
    public void saveUserTest_whenUserNull_thenThrowException() {
        Assertions.assertThatThrownBy(() -> {
            cut.saveUser(null);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void saveUserTest_whenUserNotNull_thenSaveIt() {
        // when
        User mockUser = new User(1, "username", "email@email.com", "password", "fullName", "userRole");
        User mockSavedUser = mockUser;
        when(userRepository.save(any(User.class))).thenReturn(mockSavedUser);

        // then
        User savedUser = cut.saveUser(mockUser);

        assertEquals(savedUser, mockSavedUser);
        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.user.creation", spyCaptor.getValue());
    }

    @Test
    public void updateUserTest_whenUserIdNotSame_thenThrowUserException() {

        // when
        User mockExistedUser = new User(1, "username1", "email1@email.com", "password1", "fullName1", "userRole1");
        User mockUserToUpdate = new User(2, "username2", "email2@email.com", "password2", "fullName2", "userRole2");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedUser));

        Assertions.assertThatThrownBy(() -> {
            cut.updateUser(mockUserToUpdate);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.user.not-found", spyCaptor.getValue());
    }

    @Test
    public void updateUserTest_whenUserExisted_thenUpdateUser() {
        // when
        User mockExistedUser = new User(1, "username1", "email1@email.com", "password1", "fullName1", "userRole1");
        User mockUserToUpdate = new User(1, "username2", "email2@email.com", "password2", "fullName2", "userRole2");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedUser));

        when(userRepository.save(any(User.class))).thenReturn(mockUserToUpdate);

        // then
        User updatedUser = cut.updateUser(mockUserToUpdate);

        assertEquals(updatedUser, mockUserToUpdate);

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findById(anyInt());

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.user.update", spyCaptor.getValue());
    }

    @Test
    public void updateUserTest_whenUserNoExisted_thenUpdateUser() {
        // when
        User mockUserToUpdate = new User(1, "username2", "email2@email.com", "password2", "fullName2", "userRole2");

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.updateUser(mockUserToUpdate);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.user.not-found", spyCaptor.getValue());
    }

    @Test
    public void updateUserTest_whenUserNull_thenThrowUserException() {
        Assertions.assertThatThrownBy(() -> {
            cut.updateUser(null);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void updateUserTest_whenUserIdZero_thenThrowUserException() {
        mockUser1.setId(0);
        Assertions.assertThatThrownBy(() -> {
            cut.updateUser(mockUser1);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void updateUserFromOauth2_whenOauth2UserNotExists_thenThrowException() {
        User mockUser = new User(1, "username", "email@email.com", "password", "fullName", "userRole");
        assertThrows(UserNotFoundException.class, () -> {
            cut.updateUserFromOAuth2Authentication(null, mockUser);
        });
    }

    @Test
    public void updateUserFromOauth2_whenExistingUserNotExists_thenThrowException() {
        CustomOAuth2User mockOAuth2User = null;
        assertThrows(UserNotFoundException.class, () -> {
            cut.updateUserFromOAuth2Authentication(mockOAuth2User, null);
        });
    }

    @Test
    public void updateUserFromOAuth2_whenExistingUserAndOAuth2userExist_thenUpdateUser() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        Map<String, Object> attributs = new HashMap<>();
        attributs.put("id", 123);
        attributs.put("login", "username");
        attributs.put("email", "email@gmail.com");
        attributs.put("name", "fullname");
        attributs.put("username", "username");

        CustomOAuth2User oAuth2User = new CustomOAuth2User(new DefaultOAuth2User(authorities, attributs, "username"),
                authorities, AuthProvider.GITHUB.toString(), "initialUsername");

        User mockUser = new User(1, "username", "email@email.com", "password", "fullName", "userRole");

        cut.updateUserFromOAuth2Authentication(oAuth2User, mockUser);

        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(AuthProvider.GITHUB, mockUser.getAuthenticationProvider());
        assertEquals(mockUser.getIdProvider(), (Integer) 123);

    }

    @Test
    public void deleteUserTest_whenUserNull_thenThrowUserException() {
        Assertions.assertThatThrownBy(() -> {
            cut.deleteUser(null);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteUserTest_whenUserIdZero_thenThrowUserException() {

        // when
        User mockNotExistedUser = new User(0, "username1", "email1@email.com", "password1", "fullName1", "userRole1");

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteUser(mockNotExistedUser);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.exception.not-null", spyCaptor.getValue());
    }

    @Test
    public void deleteUserTest_whenUserExisted_thenDeleteUser() {
        // when
        User mockExistedUser = new User(1, "username1", "email1@email.com", "password1", "fullName1", "userRole1");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockExistedUser));

        // then
        cut.deleteUser(mockExistedUser);
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    public void deleteUserTest_whenUserNotExisted_thenDeleteUser() {
        // when
        User mockNotExistedUser = new User(1, "username1", "email1@email.com", "password1", "fullName1", "userRole1");

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> {
            cut.deleteUser(mockNotExistedUser);
        }).isInstanceOf(UserNotFoundException.class);

        ArgumentCaptor<String> spyCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(spyCaptor.capture(), any(Object[].class), any(Locale.class));
        assertEquals("global.user.not-found", spyCaptor.getValue());
    }

}