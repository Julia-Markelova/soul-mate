package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.HelpRequest;
import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, UUID> {
    List<HelpRequest> getByCreatedBy(UUID createdBy);
    List<HelpRequest> getByCreatedByAndStatus(UUID createdBy, HelpRequestStatus status);
    List<HelpRequest> getByAcceptedBy(UUID acceptedBy);
    List<HelpRequest> getByStatus(HelpRequestStatus status);
    List<HelpRequest> getByAcceptedByAndStatus(UUID acceptedBy, HelpRequestStatus status);
}
