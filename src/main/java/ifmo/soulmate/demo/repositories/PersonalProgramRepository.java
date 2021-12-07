package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.PersonalProgram;
import ifmo.soulmate.demo.entities.enums.PersonalProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonalProgramRepository extends JpaRepository<PersonalProgram, UUID> {
    Optional<PersonalProgram> findPersonalProgramBySoulId(UUID soulId);
    List<PersonalProgram> getByStatus(PersonalProgramStatus status);
}
