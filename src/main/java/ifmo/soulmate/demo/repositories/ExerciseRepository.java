package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
}
