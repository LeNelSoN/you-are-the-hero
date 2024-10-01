package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.InvalidTokenException;
import fr.nelson.you_are_the_hero.exception.TokenExpiredException;
import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.db.RefreshToken;
import fr.nelson.you_are_the_hero.model.dto.AuthResponseDto;
import fr.nelson.you_are_the_hero.repository.RefreshTokenRepository;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.when;

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

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserService userService;

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
        when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);
        when(this.userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(User.withUsername("user")
                .password("password")
                .build());
        when(this.jwtUtil.generateToken(Mockito.any())).thenReturn("access-token-123");
        when(this.refreshTokenService.createRefreshToken(Mockito.any())).thenReturn("refresh-token-123");

        AuthResponseDto response = this.authenticationService.authenticateUser("user", "password");

        Assertions.assertNotNull(response);
        Assertions.assertEquals("access-token-123", response.getAccessToken());
        Assertions.assertEquals("refresh-token-123", response.getRefreshToken());
    }

    @Test
    public void testAuthenticateUserWithWhrongtCreds_OK() {
        when(authenticationManager.authenticate(Mockito.any())).thenThrow(new BadCredentialsException("log exception"));
        Assertions.assertThrows(Exception.class, () -> this.authenticationService.authenticateUser("user", "password"));
    }

    @Test
    public void refreshAccessToken_Success() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUsername("user");

        UserDetails userDetails = User.withUsername("user")
                .password("password")
                .roles("USER")
                .build();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("valid_refresh_token");
        refreshToken.setAppUser(appUser);
        refreshToken.setExpireAt(Instant.now().plusMillis(10000));

        when(refreshTokenRepository.findByToken("valid_refresh_token")).thenReturn(Optional.of(refreshToken));
        when(userService.loadUserByUsername("user")).thenReturn(userDetails);
        when(refreshTokenService.createRefreshToken("user")).thenReturn("new_refresh_token");
        when(jwtUtil.generateToken("user")).thenReturn("new_access_token");

        AuthResponseDto response = authenticationService.refreshAccessToken("valid_refresh_token");

        Assertions.assertNotNull(response);
        Assertions.assertEquals("new_access_token", response.getAccessToken());
        Assertions.assertEquals("new_refresh_token", response.getRefreshToken());

        Mockito.verify(refreshTokenService).deleteRefreshToken("valid_refresh_token");
    }

    @Test
    public void refreshAccessToken_InvalidToken() {
        when(refreshTokenRepository.findByToken("invalid_refresh_token")).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidTokenException.class, () -> {
            authenticationService.refreshAccessToken("invalid_refresh_token");
        });
    }

    @Test
    public void refreshAccessToken_ExpiredToken() {
        AppUser appUser = new AppUser();
        appUser.setUsername("user");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("valid_refresh_token");
        refreshToken.setAppUser(appUser);
        refreshToken.setExpireAt(Instant.now().minusMillis(10000));

        when(refreshTokenRepository.findByToken("valid_refresh_token")).thenReturn(Optional.of(refreshToken));

        Assertions.assertThrows(TokenExpiredException.class, () -> {
            authenticationService.refreshAccessToken("valid_refresh_token");
        });

        Mockito.verify(refreshTokenService).deleteRefreshToken("valid_refresh_token");
    }

    @Test
    public void refreshAccessToken_UserNotFound() {
        AppUser appUser = new AppUser();
        appUser.setUsername("user");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("valid_refresh_token");
        refreshToken.setAppUser(appUser);
        refreshToken.setExpireAt(Instant.now().plusMillis(10000));

        when(refreshTokenRepository.findByToken("valid_refresh_token")).thenReturn(Optional.of(refreshToken));
        when(userService.loadUserByUsername("user")).thenThrow(new UsernameNotFoundException("User not found"));

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.refreshAccessToken("valid_refresh_token");
        });
    }
}

