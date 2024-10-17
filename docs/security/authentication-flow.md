# Authentication Flow

Our application uses JWT-based authentication. Here's the flow:

1. User registers via `/auth/register` endpoint
2. User logs in via `/auth/login` endpoint
3. Upon successful authentication, an access token is generated and returned
4. The client includes this token in the Authorization header for subsequent requests
5. The `AuthenticationFilter` intercepts each request to validate the token

## Key Components

- `AuthenticationController`: Handles registration and login
- `AuthenticationService`: Authenticates users and generates tokens
- `AuthenticationFilter`: Validates tokens for each request

[See Token Handling for more details on token generation and validation](token-handling.md)