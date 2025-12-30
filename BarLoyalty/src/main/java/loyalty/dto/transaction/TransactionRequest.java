package loyalty.dto.transaction;

import java.util.UUID;

public record TransactionRequest(
        UUID barId,
        int points
) {
}
