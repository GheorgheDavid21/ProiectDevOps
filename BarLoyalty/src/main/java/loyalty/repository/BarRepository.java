package loyalty.repository;

import loyalty.entity.Bar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BarRepository extends JpaRepository<Bar, UUID> {
    Optional<Bar> findByAdminId(UUID adminId);
}
