package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.utility.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticateUser(String username, String password) throws Exception {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BadRequestException("USERNAME_OR_PASSWORD_CAN_NOT_BE_NULL");
        }
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            return jwtUtil.generateToken(userDetails.getUsername());

        } catch (AuthenticationException e) {
            throw new Exception("Invalid username or password", e);
        }
    }
}

