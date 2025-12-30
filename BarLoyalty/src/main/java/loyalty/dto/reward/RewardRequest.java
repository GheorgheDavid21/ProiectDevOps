package loyalty.dto.reward;

public record RewardRequest (
    String name,
    String description,
    int pointsRequired
){}
