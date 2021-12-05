package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Life;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ILifesRepository extends JpaRepository<Life, UUID> {

    Optional<Life> getLifeBySoulIdAndDateOfDeathIsNull(UUID soulId);

    List<Life> getLifeBySoulId(UUID soulId);
}
