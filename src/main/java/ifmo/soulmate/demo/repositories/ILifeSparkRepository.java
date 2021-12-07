package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.LifeSpark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ILifeSparkRepository  extends JpaRepository<LifeSpark, UUID> {
    List<LifeSpark> getByPersonalProgramIdNotNull();
}
