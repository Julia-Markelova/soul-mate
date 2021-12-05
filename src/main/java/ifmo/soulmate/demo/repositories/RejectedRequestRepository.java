package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.RejectedRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RejectedRequestRepository extends JpaRepository<RejectedRequest, UUID> {
    @Query(value = "select CAST(request_id as varchar) request_id from rejected_requests where rejected_by = ?1 ",
            nativeQuery = true)
    public List<UUID> getRejectedRequestIdsByRejectedById(UUID rejectedBy);
    @Query(value = "select CAST(request_id as varchar) request_id from rejected_requests where rejected_by = ?1 ",
            nativeQuery = true)
    public List<UUID> getRelativesWithAllowedNotificationsForSoulId(UUID soulId);

}
