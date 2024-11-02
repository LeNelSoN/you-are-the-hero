package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.AdminAlreadyExistException;
import fr.nelson.you_are_the_hero.exception.UserAllreadyExistException;
import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.db.Role;
import fr.nelson.you_are_the_hero.model.dto.AuthRequestDto;
import fr.nelson.you_are_the_hero.model.dto.UserDto;
import fr.nelson.you_are_the_hero.repository.AppUserRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    @Test
    public void test_getCurrentUsername_OK() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("Jean Neige");

        String currentUsername = userService.getCurrentUsername();
        assertEquals("Jean Neige", currentUsername);
    }

    @Test
    public void test_getCurrentUsername_NotAuthenticated() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        String currentUsername = userService.getCurrentUsername();
        assertEquals("", currentUsername);
    }

    @Test
    public void test_getCurrentUsername_NullAuthentication() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(null);

        String currentUsername = userService.getCurrentUsername();
        assertEquals("", currentUsername);
    }

    @Test
    public void test_createNewUser_OK() throws Exception {
        // Given
        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setPassword("my-password");
        authRequestDto.setUsername("my-username");


        when(appUserRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        when(appUserRepository.save(Mockito.any())).thenReturn(new AppUser());
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("toto");

        // When
        AppUser result = userService.saveUser(authRequestDto);

        // Then
        Assertions.assertNotNull(result);
        verify(appUserRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void test_createNewUserWithExistingOne_ThrowsException() {
        // Given
        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setPassword("my-password");
        authRequestDto.setUsername("my-username");

        // When
        when(appUserRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        // Then
        Assertions.assertThrows(UserAllreadyExistException.class, () -> this.userService.saveUser(authRequestDto));
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        AppUser appUser = new AppUser();
        appUser.setUsername("jean neige");
        appUser.setPassword("Ygrid");
        appUser.setRole(Role.PLAYER);

        when(appUserRepository.findByUsername("jean neige")).thenReturn(appUser);

        UserDetails userDetails = userService.loadUserByUsername("jean neige");

        assertNotNull(userDetails);
        assertEquals("jean neige", userDetails.getUsername());
        assertEquals("Ygrid", userDetails.getPassword());
    }

    @Test
    public void testFindAppUserByUsername_UserExists() {
        AppUser appUser = new AppUser();
        appUser.setUsername("jean neige");
        appUser.setPassword("Ygrid");
        appUser.setRole(Role.PLAYER);

        when(appUserRepository.findByUsername("jean neige")).thenReturn(appUser);

        AppUser user = userService.findAppUserByUsername("jean neige");

        assertNotNull(user);
        assertEquals("jean neige", user.getUsername());
    }

    @Test
    public void testFindAppUserByUsername_UserNotFound() {
        when(appUserRepository.findByUsername("unknown-username")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("unknown-username");
        });

    }

    @Test
    public void test_deleteUserByUsername_ok() throws BadRequestException {
        AppUser user = new AppUser();
        user.setPassword("123");
        user.setRole(Role.PLAYER);
        user.setUsername("username");
        User testUser = new User("username", "123", true, false, false, false, new ArrayList<>());
        when(this.appUserRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(this.appUserRepository.save(Mockito.any())).thenReturn(user);
        this.userService.deleteByUsername("username", testUser);
    }

    @Test
    public void test_deleteUserByAdmin_ok() throws BadRequestException {
        AppUser user = new AppUser();
        user.setPassword("123");
        user.setRole(Role.ADMIN);
        user.setUsername("username");
        User testUser = new User("usernameAdmin", "123", true, false, false, false, new ArrayList<>());
        when(this.appUserRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(this.appUserRepository.save(Mockito.any())).thenReturn(user);
        this.userService.deleteByUsername("username", testUser);
    }

    @Test
    public void test_deleteUserWrongUserConnected_NoAdmin_ok() throws BadRequestException {
        AppUser user = new AppUser();
        user.setPassword("123");
        user.setRole(Role.PLAYER);
        user.setUsername("usernamePlayer");
        User testUser = new User("usernamePlayer", "123", true, false, false, false, new ArrayList<>());
        when(this.appUserRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        this.userService.deleteByUsername("username", testUser);
    }

    @Test
    public void testCreateAdminUser_Ok() {
        AppUser appUser = new AppUser();
        appUser.setUsername("jean neige");
        appUser.setPassword("Ygrid");
        appUser.setRole(Role.PLAYER);

        UserDto userDto = new UserDto("jean neige");

        when(userService.isAlreadyAnAdmin()).thenReturn(false);
        when(appUserRepository.findByUsername("jean neige")).thenReturn(appUser);
        when(appUserRepository.save(appUser)).thenReturn(appUser);

        AppUser adminUser = userService.createAdminUser(userDto);

        assertNotNull(adminUser);
        assertEquals("jean neige", adminUser.getUsername());
        assertEquals(Role.ADMIN, adminUser.getRole());

        verify(appUserRepository).save(appUser);
    }

    @Test
    public void testCreateAdminUser_AdminExists() {
        UserDto userDto = new UserDto("jean neige");

        when(userService.isAlreadyAnAdmin()).thenReturn(true);

        assertThrows(AdminAlreadyExistException.class, () -> {
            userService.createAdminUser(userDto);
        });
    }

    @Test
    public void testCreateAdminUser_WithInexistingAppUser() {
        UserDto userDto = new UserDto("jean neige");

        when(appUserRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.createAdminUser(userDto);
        });
    }

    @Test
    public void testIsAlreadyAnAdmin_NoAdminExist() {
        when(appUserRepository.existsByRole(Role.ADMIN)).thenReturn(false);

        Boolean isAnAdmin = userService.isAlreadyAnAdmin();

        assertFalse(isAnAdmin);
    }

    @Test
    public void testIsAlreadyAnAdmin_AdminExist() {
        when(appUserRepository.existsByRole(Role.ADMIN)).thenReturn(true);

        Boolean isAnAdmin = userService.isAlreadyAnAdmin();

        assertTrue(isAnAdmin);
    }

    @Test
    void promoteUserToEditor_SuccessfulPromotion() throws BadRequestException {
        AppUser user = new AppUser();
        user.setUsername("Perceval");
        user.setRole(Role.PLAYER);

        when(appUserRepository.findByUsername("Perceval")).thenReturn(user);
        when(appUserRepository.save(user)).thenReturn(user);

        AppUser promotedUser = userService.promoteUserToEditor("Perceval");

        assertEquals(Role.EDITOR, promotedUser.getRole());
        verify(appUserRepository).save(user);
    }

    @Test
    void promoteUserToEditor_UserAlreadyEditor() {
        AppUser user = new AppUser();
        user.setUsername("Perceval");
        user.setRole(Role.EDITOR);

        when(appUserRepository.findByUsername("Perceval")).thenReturn(user);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.promoteUserToEditor("Perceval");
        });

        assertEquals("USER_ALREADY_EDITOR", exception.getMessage());
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void promoteUserToEditor_UserNotFound() {
        when(appUserRepository.findByUsername("unknownUser")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.promoteUserToEditor("unknownUser");
        });

        assertEquals("User Not Found", exception.getMessage());
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void promoteUserToEditor_EmptyUsername() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.promoteUserToEditor("");
        });

        assertEquals("EMPTY_PARAMETER", exception.getMessage());
        verify(appUserRepository, never()).findByUsername(any());
        verify(appUserRepository, never()).save(any());
    }
}
