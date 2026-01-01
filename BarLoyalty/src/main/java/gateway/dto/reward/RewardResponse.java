package gateway.dto.reward;

import gateway.entity.Reward;

import java.util.UUID;

public record RewardResponse (
        UUID id,
        String name,
        String description,
        int pointsRequired,
        boolean active,
        UUID barId
) {
   public static RewardResponse fromEntity(Reward reward) {
        return new RewardResponse(
                reward.getId(),
                reward.getName(),
                reward.getDescription(),
                reward.getPointsRequired(),
                reward.isActive(),
                reward.getBar().getId()
        );
    }
}
