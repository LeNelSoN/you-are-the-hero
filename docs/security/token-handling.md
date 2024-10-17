# Token Handling

This document explains how tokens are generated and validated in our application.

## Token Generation

Tokens are generated in the `AuthenticationService`:


The actual token creation happens in `JwtUtil`:


## Token Validation

Tokens are validated in the `AuthenticationFilter`:



[See Security Configuration for details on how this filter is applied](security-configuration.md)

