package gateway.repository;

import gateway.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByQrCode(String qrCode);
    List<Transaction> findByUserId(UUID userId);
}
