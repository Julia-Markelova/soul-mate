package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.SystemMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<SystemMode, UUID> {
}
