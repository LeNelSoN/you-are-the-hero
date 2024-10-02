package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.InvalidCredentialsException;
import fr.nelson.you_are_the_hero.exception.InvalidTokenException;
import fr.nelson.you_are_the_hero.exception.TokenExpiredException;
import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.db.RefreshToken;
import fr.nelson.you_are_the_hero.model.dto.AuthResponseDto;
import fr.nelson.you_are_the_hero.repository.RefreshTokenRepository;
import fr.nelson.you_are_the_hero.utility.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    public AuthResponseDto authenticateUser(String username, String password) throws InvalidCredentialsException, BadRequestException {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BadRequestException("USERNAME_OR_PASSWORD_CAN_NOT_BE_NULL");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            final String accessToken = jwtUtil.generateToken(userDetails.getUsername());
            final String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            return new AuthResponseDto(accessToken, refreshToken);

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public AuthResponseDto refreshAccessToken(String refreshToken) throws InvalidTokenException, TokenExpiredException {
        RefreshToken storedRefreshToken = refreshTokenService.getRefreshToken(refreshToken);

        if (storedRefreshToken.getExpireAt().isBefore(Instant.now())) {
            refreshTokenService.deleteRefreshToken(storedRefreshToken.getToken());
            throw new TokenExpiredException("Refresh token has expired");
        }

        AppUser user = storedRefreshToken.getAppUser();

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        String newRefreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        String newAccessToken = jwtUtil.generateToken(userDetails.getUsername());

        refreshTokenService.deleteRefreshToken(storedRefreshToken.getToken());

        return new AuthResponseDto(newAccessToken, newRefreshToken);
    }

}
