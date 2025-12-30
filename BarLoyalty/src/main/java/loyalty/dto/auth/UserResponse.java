package loyalty.dto.auth;

import loyalty.entity.User;

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
