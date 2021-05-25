package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.SoulRelative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ISoulRelativeRepository extends JpaRepository<SoulRelative, UUID> {
}
