package ifmo.soulmate.demo.services;


import ifmo.soulmate.demo.entities.God;
import ifmo.soulmate.demo.entities.HelpRequest;
import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.HelpRequestType;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.HelpRequestDto;
import ifmo.soulmate.demo.repositories.HelpRequestRepository;
import ifmo.soulmate.demo.repositories.SoulRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MentorService {
    @Autowired
    private SoulRepository soulRepository;

    @Autowired
    private HelpRequestRepository helpRequestRepository;

    private static final Logger log = LogManager.getLogger(GodService.class);

    public List<HelpRequestDto> getOpenHelpRequests(UUID soulId) {
        if (checkIsMentor(soulId)) {
            List<HelpRequest> newRequests = helpRequestRepository.getByStatusAndType(HelpRequestStatus.NEW, HelpRequestType.MENTOR);
            List<HelpRequest> acceptedRequests = helpRequestRepository.getByAcceptedByAndStatus(soulId, HelpRequestStatus.ACCEPTED);
            List<HelpRequest> openRequests = Stream.concat(newRequests.stream(), acceptedRequests.stream())
                    .collect(Collectors.toList());
            log.info("Get open requests by mentor {}", soulId);
            return mapResult(openRequests);
        }
        String msg = String.format("Expected soul with isMentor = true");
        log.warn(msg);
        throw new IllegalArgumentException(msg);
    }


    public List<HelpRequestDto> getHelpRequestsByMentorId(UUID soulId) {
        if (checkIsMentor(soulId)) {
            List<HelpRequest> requests = helpRequestRepository.getByAcceptedBy(soulId);
            log.info("Get all requests for mentor {}", soulId);
            return mapResult(requests);
        }
        String msg = String.format("Expected soul with isMentor = true");
        log.warn(msg);
        throw new IllegalArgumentException(msg);
    }

    public List<HelpRequestDto> getHelpRequestsByMentorIdAndStatus(UUID soulId, HelpRequestStatus status) {
        if (checkIsMentor(soulId)) {
            List<HelpRequest> requests = helpRequestRepository.getByAcceptedByAndStatus(soulId, status);
            log.info("Get requests by status {} for god {}", status, soulId);
            return mapResult(requests);
        }
        String msg = String.format("Expected soul with isMentor = true");
        log.warn(msg);
        throw new IllegalArgumentException(msg);
    }

    public HelpRequestDto updateStatusForRequest(UUID soulId, UUID requestId, HelpRequestStatus status) throws NonExistingEntityException {
        Optional<HelpRequest> request = helpRequestRepository.findById(requestId);
        if (!request.isPresent() || !checkIsMentor(soulId)) {
            String msg = (String.format("Soul is not mentor: %s or no request found with id %s",
                    soulId.toString(), requestId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        HelpRequest req = request.get();
        if (req.getStatus() == HelpRequestStatus.NEW && status == HelpRequestStatus.ACCEPTED ||
                req.getStatus() == HelpRequestStatus.ACCEPTED && status == HelpRequestStatus.FINISHED) {
            log.info("Update request {} by mentor {}. Set status {}", requestId, soulId, status);
            req.setStatus(status);
            req.setAcceptedBy(soulId);
            helpRequestRepository.saveAndFlush(req);
            return new HelpRequestDto(req.getId().toString(), req.getCreatedBy().toString(), req.getAcceptedBy().toString(), req.getStatus());
        } else {
            throw new IllegalArgumentException("Can not update such request with such status");
        }
    }

    private List<HelpRequestDto> mapResult(List<HelpRequest> requests) {
        return requests
                .stream()
                .map(x -> {
                    try {
                        return new HelpRequestDto(
                                x.getId().toString(), x
                                .getCreatedBy().toString(),
                                (x.getAcceptedBy() != null) ? x.getAcceptedBy().toString() : null,
                                x.getStatus());
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean checkIsMentor(UUID soulId) {
        Optional<Soul> mentor = soulRepository.findById(soulId);
        return mentor.map(Soul::getIsMentor).orElse(false);
    }
}
