# Future Improvements

While our current implementation provides secure authentication, here are some potential improvements:

## Implementing Refresh Tokens

Currently, we only use access tokens. Adding refresh tokens would allow:
- Shorter lifespans for access tokens
- Improved security through token rotation

Implementation steps:
1. Modify `JwtUtil` to generate both access and refresh tokens
2. Store refresh tokens securely (e.g., in a database)
3. Add a token refresh endpoint
4. Implement refresh token rotation

## Token Revocation

Implement a mechanism to invalidate tokens on user logout or suspected security breaches.

## Enhanced Token Payload

Consider adding more claims to the token payload (e.g., user roles) to reduce database lookups for authorization checks.