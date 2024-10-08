# Spring Security Documentation: JWT Authentication

## Table of Contents
1. [Introduction](#introduction)
2. [Security Configuration](#security-configuration)
3. [Authentication Flow](#authentication-flow)
4. [Access Tokens](#access-tokens)
5. [Token Generation and Validation](#token-generation-and-validation)
6. [Security Considerations](#security-considerations)
7. [Future Improvements](#future-improvements)

## Introduction

This application uses Spring Security with JSON Web Tokens (JWTs) for authentication. The system currently implements access tokens for securing endpoints and authenticating users.

## Security Configuration

The security configuration is defined in the `SecurityConfig` class:
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
// ... (other beans and autowired fields)
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http.csrf().disable()
.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
authorizationManagerRequestMatcherRegistry
.requestMatchers("/","/auth/").permitAll()
.anyRequest().authenticated())
.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
return http.build();
}
// ... (other beans)
}
```
This configuration:
- Disables CSRF protection (as we're using stateless JWTs)
- Allows public access to "/" and "/auth/**" endpoints
- Requires authentication for all other requests
- Sets the session creation policy to stateless
- Adds our custom `AuthenticationFilter` before the default `UsernamePasswordAuthenticationFilter`
## Authentication Flow

1. User registers via `/auth/register` endpoint (AuthenticationController.registerUser)
2. User logs in via `/auth/login` endpoint (AuthenticationController.authenticateUser)
3. Upon successful authentication, an access token is generated and returned
4. The client includes this token in the Authorization header for subsequent requests
5. The `AuthenticationFilter` intercepts each request to validate the token
## Access Tokens

Access tokens are JWTs that contain encoded information about the authenticated user. In our application:

- Tokens are generated using the `JwtUtil` class
- They have a validity period of 10 hours (configurable via the `VALIDITY` constant in `JwtUtil`)
- Tokens contain the username as the subject claim

### Purpose and Functionality

Access tokens serve to:
1. Authenticate the user for each request
2. Maintain a stateless session
3. Carry user information (username in this case)

## Token Generation and Validation

### Generating Tokens

Tokens are generated in the `AuthenticationService`:
```java
@Service
public class AuthenticationService {
// ... (autowired fields)
public String authenticateUser(String username, String password) throws Exception {
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
The actual token generation happens in the `JwtUtil` class:
```java
java:fr/nelson/you_are_the_hero/utility/JwtUtil.java
@Component
public class JwtUtil {
// ... (fields and other methods)
public String generateToken(String username) {
Map<String, Object> claims = new HashMap<>();
return createToken(claims, username);
}
private String createToken(Map<String, Object> claims, String subject) {
return Jwts.builder()
.setClaims(claims)
.setSubject(subject)
.setIssuedAt(new Date(System.currentTimeMillis()))
.setExpiration(new Date(System.currentTimeMillis() + VALIDITY))
.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
.compact();
}
}

### Validating Tokens

Token validation occurs in the `AuthenticationFilter`:


## Security Considerations

1. Ensure all sensitive endpoints are properly secured in the `SecurityConfig`.
2. Use HTTPS in production to prevent token interception.
3. Keep the JWT secret key (`SECRET_KEY` in `JwtUtil`) secure and consider using environment variables for sensitive configuration.
4. Regularly review and update dependencies to address any security vulnerabilities.

## Future Improvements

### Implementing Refresh Tokens

Currently, the application does not use refresh tokens. Implementing them would enhance security by allowing shorter lifespans for access tokens. To add refresh tokens:

1. Modify the `JwtUtil` to generate both access and refresh tokens.
2. Store refresh tokens securely (e.g., in a database).
3. Add an endpoint for token refresh, e.g
```java
@postmapping("/refresh"){
public ResponseEntity<?> refreshToken(@responseBody RefreshTokenRequest request)

// validate refresh..

}
```







