package loyalty.dto.auth;

public record LoginRequest(
        String email,
        String password
) {}
