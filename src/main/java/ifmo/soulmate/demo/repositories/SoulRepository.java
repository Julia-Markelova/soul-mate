package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Soul;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoulRepository extends JpaRepository<Soul, Long> {
}
