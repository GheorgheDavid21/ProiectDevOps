package gateway.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gateway.util.TransactionStatus;
import gateway.util.TransactionType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Bar bar;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private int points;

    private String qrCode;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Instant createdAt = Instant.now();

}
