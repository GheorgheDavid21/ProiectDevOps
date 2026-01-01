package gateway.dto.auth;

import gateway.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        int points
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getPoints()
        );
    }
}
