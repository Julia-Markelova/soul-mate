package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.HelpRequest;
import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.HelpRequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, UUID> {
    List<HelpRequest> getByCreatedByAndType(UUID createdBy, HelpRequestType type);
    List<HelpRequest> getByCreatedByAndStatusAndType(UUID createdBy, HelpRequestStatus status, HelpRequestType type);
    List<HelpRequest> getByAcceptedBy(UUID acceptedBy);
    List<HelpRequest> getByStatusAndType(HelpRequestStatus status, HelpRequestType type);
    List<HelpRequest> getByAcceptedByAndStatus(UUID acceptedBy, HelpRequestStatus status);
}
