package loyalty.dto.auth;

import loyalty.util.Role;

public record RegisterRequest(
        String email,
        String password,
        Role role
) {}
