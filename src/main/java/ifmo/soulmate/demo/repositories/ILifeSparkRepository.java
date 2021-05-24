package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.models.LifeSpark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILifeSparkRepository  extends JpaRepository<LifeSpark, Long> {
}
