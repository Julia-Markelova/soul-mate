package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Life;
import ifmo.soulmate.demo.entities.SoulRelative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ILifesRepository extends JpaRepository<Life, UUID> {
}
