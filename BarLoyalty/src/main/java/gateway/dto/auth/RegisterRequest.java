package gateway.dto.auth;

import gateway.util.Role;

public record RegisterRequest(
        String email,
        String password,
        Role role
) {}
