package fr.nelson.you_are_the_hero.filter;

import fr.nelson.you_are_the_hero.service.ApiKeyService;
import fr.nelson.you_are_the_hero.service.FailedAttemptService;
import fr.nelson.you_are_the_hero.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    ApiKeyService apiKeyService;

    @Autowired
    UserService userService;

    @Autowired
    FailedAttemptService failedAttemptService;

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!apiKeyService.isComformApiKey()){
            logger.error("Your Admin Key is no confrom to Base64 256bit");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String userIp = request.getRemoteAddr();

        if (failedAttemptService.isBlocked(userIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            logger.warn("User with IP {} was blocked", userIp);
            return;
        }

        if(request.getRequestURI().startsWith("/auth/admin")){
            if(userService.isAlreadyAnAdmin()){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String apiKeyHeader = request.getHeader("ADMIN_KEY");

            if(apiKeyHeader == null || !apiKeyService.isValidApiKey(apiKeyHeader)){
                failedAttemptService.registerFailedAttempt(userIp);
                logger.info("Admin key used is incorrect; {}", userIp);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            failedAttemptService.resetAttempts(userIp);
        }

        filterChain.doFilter(request, response);
    }
}
