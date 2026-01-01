package gateway.dto.auth;

public record LoginRequest(
        String email,
        String password
) {}
