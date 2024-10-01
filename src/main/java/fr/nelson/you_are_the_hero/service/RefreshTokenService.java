package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.model.db.RefreshToken;
import fr.nelson.you_are_the_hero.repository.RefreshTokenRepository;
import fr.nelson.you_are_the_hero.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${jwt.refresh-validity}")
    private int REFRESH_TOKEN_VALIDITY;

    public String createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(generateRefreshtoken());
        refreshToken.setAppUser(userService.findAppUserByUsername(username));
        refreshToken.setExpireAt(Instant.now().plusMillis(REFRESH_TOKEN_VALIDITY));

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public void deleteRefreshToken(String refreshToken){
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    private String generateRefreshtoken(){
        return UUID.randomUUID().toString();
    }


}
