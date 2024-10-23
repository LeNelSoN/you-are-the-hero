package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.InvalidTokenException;
import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.db.RefreshToken;
import fr.nelson.you_are_the_hero.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserService userService;

    @Test
    void testCreateRefreshToken_Success() {
        String username = "user";
        AppUser appUser = new AppUser();
        appUser.setUsername(username);

        when(userService.findAppUserByUsername(username)).thenReturn(appUser);

        String refreshToken = refreshTokenService.createRefreshToken(username);

        Assertions.assertNotNull(refreshToken);

        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testDeleteRefreshToken_Success() {
        String refreshToken = "test-refresh-token";

        refreshTokenService.deleteRefreshToken(refreshToken);

        verify(refreshTokenRepository, times(1)).deleteByToken(refreshToken);
    }

    @Test
    public void getRefreshToken_ValidToken() throws InvalidTokenException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("valid_refresh_token");

        when(refreshTokenRepository.findByToken("valid_refresh_token")).thenReturn(Optional.of(refreshToken));

        RefreshToken result = refreshTokenService.getRefreshToken("valid_refresh_token");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("valid_refresh_token", result.getToken());
    }

    @Test
    public void getRefreshToken_InvalidToken() {
        when(refreshTokenRepository.findByToken("invalid_refresh_token")).thenReturn(Optional.empty());

        InvalidTokenException exception = Assertions.assertThrows(InvalidTokenException.class, () -> {
            refreshTokenService.getRefreshToken("invalid_refresh_token");
        });

        Assertions.assertEquals("Invalid refresh token", exception.getMessage());
    }
}
