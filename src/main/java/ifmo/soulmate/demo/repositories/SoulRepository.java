package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.models.Soul;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoulRepository extends JpaRepository<Soul, Long> {
}
