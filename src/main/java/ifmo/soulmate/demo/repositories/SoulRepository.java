package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Soul;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SoulRepository extends JpaRepository<Soul, UUID> {
    Optional<Soul> getByUserId(UUID userId);
}
