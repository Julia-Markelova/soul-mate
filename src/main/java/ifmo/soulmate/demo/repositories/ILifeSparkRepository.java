package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.LifeSpark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILifeSparkRepository  extends JpaRepository<LifeSpark, Long> {
}
