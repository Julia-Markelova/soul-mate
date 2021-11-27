package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.God;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GodRepository extends JpaRepository<God, UUID> {
    Optional<God> getByUserId(UUID userId);
}
