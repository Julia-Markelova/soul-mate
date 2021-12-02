package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.ProgramExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgramExerciseRepository extends JpaRepository<ProgramExercise, UUID> {
    List<ProgramExercise> findProgramExerciseByProgramId(UUID programId);
    Optional<ProgramExercise> findProgramExerciseByProgramIdAndExerciseId(UUID programId, UUID exerciseId);
}
