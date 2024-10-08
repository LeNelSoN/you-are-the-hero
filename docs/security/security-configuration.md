# Security Configuration

Our security settings are configured in the `SecurityConfig` class:


Key points:
- CSRF is disabled (we use stateless JWTs)
- Public access to "/" and "/auth/**" endpoints
- Authentication required for all other requests
- Stateless session management
- Custom `AuthenticationFilter` added to the filter chain
