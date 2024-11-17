# Access Tokens

Our application uses JSON Web Tokens (JWTs) as access tokens.

## Characteristics

- Generated using the `JwtUtil` class
- Validity period: 10 hours (configurable via `VALIDITY` constant in `JwtUtil`)
- Payload contains the username as the subject claim

## Purpose

1. Authenticate the user for each request
2. Maintain a stateless session
3. Carry user information (username)

[See Token Handling for details on token generation and validation](token-handling.md)