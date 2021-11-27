package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.SystemMode;
import ifmo.soulmate.demo.entities.enums.SystemModeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SystemModeRepository extends JpaRepository<SystemMode, UUID> {
    SystemMode findSystemModeByType(SystemModeType type);
}
