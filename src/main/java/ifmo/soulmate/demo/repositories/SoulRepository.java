package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SoulRepository extends JpaRepository<Soul, UUID> {
    Optional<Soul> getByUserId(UUID userId);
    List<Soul> getByStatus(SoulStatus status);
}
