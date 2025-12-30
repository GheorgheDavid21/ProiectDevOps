package loyalty.dto.transaction;

import loyalty.entity.Transaction;
import loyalty.util.TransactionStatus;
import loyalty.util.TransactionType;

import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID userId,
        UUID barId,
        TransactionType type,
        TransactionStatus status,
        int points,
        String qrCode,
        Instant createdAt

) {
    public static TransactionResponse fromEntity(Transaction tr) {
        return new TransactionResponse(
                tr.getId(),
                tr.getUser().getId(),
                tr.getBar().getId(),
                tr.getType(),
                tr.getStatus(),
                tr.getPoints(),
                tr.getQrCode(),
                tr.getCreatedAt()
        );
    }
}
