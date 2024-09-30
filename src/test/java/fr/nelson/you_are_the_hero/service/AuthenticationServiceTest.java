package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.utility.JwtUtil;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void testAuthenticateUserWithNullUsername_throwBRE() {
        Assertions.assertThrows(BadRequestException.class, () -> this.authenticationService.authenticateUser(null, "123"));
    }

    @Test
    public void testAuthenticateUserWithEmptyUsername_throwBRE() {
        Assertions.assertThrows(BadRequestException.class, () -> this.authenticationService.authenticateUser("", "123"));
    }

    @Test
    public void testAuthenticateUserWithNullPassword_throwBRE() {
        Assertions.assertThrows(BadRequestException.class, () -> this.authenticationService.authenticateUser("user", null));
    }

    @Test
    public void testAuthenticateUserWithEmptyPassword_throwBRE() {
        Assertions.assertThrows(BadRequestException.class, () -> this.authenticationService.authenticateUser("user", ""));
    }

    @Test
    public void testAuthenticateUserWithRightCreds_OK() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);
        Mockito.when(this.userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(User.withUsername("user")
                .password("password")
                .build());
        Mockito.when(this.jwtUtil.generateToken(Mockito.any())).thenReturn("123");
        Assertions.assertEquals("123", this.authenticationService.authenticateUser("user", "password"));
    }

    @Test
    public void testAuthenticateUserWithWhrongtCreds_OK() {
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(new BadCredentialsException("log exception"));
        Assertions.assertThrows(Exception.class, () -> this.authenticationService.authenticateUser("user", "password"));
    }

}

