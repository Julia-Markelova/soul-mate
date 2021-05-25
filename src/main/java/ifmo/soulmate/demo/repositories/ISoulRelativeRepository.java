package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.SoulRelative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ISoulRelativeRepository extends JpaRepository<SoulRelative, UUID> {
    @Query(value = "select CAST(relative_id as varchar) relative_id from soul_relatives where soul_id = ?1 and notify_relative_about_soul = true ",
            nativeQuery = true)
    public List<UUID> getRelativesWithAllowedNotificationsForSoulId(UUID soulId);
}
