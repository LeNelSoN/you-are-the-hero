package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.db.RefreshToken;
import fr.nelson.you_are_the_hero.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
