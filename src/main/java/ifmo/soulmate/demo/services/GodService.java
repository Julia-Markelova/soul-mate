package ifmo.soulmate.demo.services;


import ifmo.soulmate.demo.entities.God;
import ifmo.soulmate.demo.entities.HelpRequest;
import ifmo.soulmate.demo.entities.RejectedRequest;
import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.HelpRequestType;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.GodDto;
import ifmo.soulmate.demo.models.HelpRequestDto;
import ifmo.soulmate.demo.repositories.GodRepository;
import ifmo.soulmate.demo.repositories.HelpRequestRepository;
import ifmo.soulmate.demo.repositories.RejectedRequestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GodService {
    @Autowired
    private GodRepository godRepository;

    @Autowired
    private HelpRequestRepository helpRequestRepository;
    @Autowired
    private RejectedRequestRepository rejectedRequestRepository;

    private static final Logger log = LogManager.getLogger(GodService.class);

    public GodDto getGodById(UUID godId) throws NonExistingEntityException {
        Optional<God> god = godRepository.findById(godId);
        if (!god.isPresent()) {
            String msg = (String.format("No god found with id: %s", godId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        log.info("Get god by id {}", godId);
        God unwrapped = god.get();
        return new GodDto(unwrapped.getId().toString(), unwrapped.getUserId().toString(), unwrapped.getName());
    }

    public GodDto getGodByUserId(UUID userId) throws NonExistingEntityException {
        Optional<God> god = godRepository.getByUserId(userId);
        if (!god.isPresent()) {
            String msg = (String.format("No god found with userId: %s", userId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        log.info("Get god by userId {}", userId);
        God unwrapped = god.get();
        return new GodDto(unwrapped.getId().toString(), unwrapped.getUserId().toString(), unwrapped.getName());
    }

    public List<HelpRequestDto> getOpenHelpRequests(UUID godId) {
        List<HelpRequest> newRequests = helpRequestRepository.getByStatusAndType(HelpRequestStatus.NEW, HelpRequestType.GOD);
        List<HelpRequest> acceptedRequests = helpRequestRepository.getByAcceptedByAndStatus(godId, HelpRequestStatus.ACCEPTED);
        List<UUID> rejectedRequestsIds = rejectedRequestRepository.getRejectedRequestIdsByRejectedById(godId);
        Set<UUID> rejectedRequestsIdsSet = new HashSet<>(rejectedRequestsIds);
        List<HelpRequest> openRequests = Stream.concat(newRequests.stream(), acceptedRequests.stream())
                .collect(Collectors.toList());
        openRequests = openRequests.stream().filter(e -> !rejectedRequestsIdsSet.contains(e.getId())).collect(Collectors.toList());
        log.info("Get open requests by god {}", godId);
        return mapResult(openRequests);
    }

    public List<HelpRequestDto> getHelpRequestsByGodId(UUID godId) {
        List<HelpRequest> requests = helpRequestRepository.getByAcceptedBy(godId);
        log.info("Get all requests for god {}", godId);
        return mapResult(requests);
    }

    public List<HelpRequestDto> getHelpRequestsByGodIdAndStatus(UUID godId, HelpRequestStatus status) {
        List<HelpRequest> requests = helpRequestRepository.getByAcceptedByAndStatus(godId, status);
        log.info("Get requests by status {} for god {}", status, godId);
        return mapResult(requests);
    }

    public HelpRequestDto updateStatusForRequest(UUID godId, UUID requestId, HelpRequestStatus status) throws NonExistingEntityException {
        Optional<HelpRequest> request = helpRequestRepository.findById(requestId);
        Optional<God> god = godRepository.findById(godId);
        if (!request.isPresent() || !god.isPresent()) {
            String msg = (String.format("No god found with id: %s or no request found with id %s",
                    godId.toString(), requestId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }

        HelpRequest req = request.get();
        if (req.getStatus() == HelpRequestStatus.NEW && status == HelpRequestStatus.ACCEPTED ||
                req.getStatus() == HelpRequestStatus.ACCEPTED && status == HelpRequestStatus.FINISHED) {
            log.info("Update request {} by god {}. Set status {}", requestId, godId, status);
            req.setStatus(status);
            req.setAcceptedBy(godId);
            helpRequestRepository.saveAndFlush(req);
            return new HelpRequestDto(req.getId().toString(), req.getCreatedBy().toString(), req.getAcceptedBy().toString(), req.getStatus());
        } else {
            throw new IllegalArgumentException("Can not update such request with such status");
        }
    }

    public void rejectRequest(UUID godId, UUID requestId) throws NonExistingEntityException {
        Optional<HelpRequest> request = helpRequestRepository.findById(requestId);
        Optional<God> god = godRepository.findById(godId);
        if (!request.isPresent() || !god.isPresent()) {
            String msg = (String.format("No god found with id: %s or no request found with id %s",
                    godId.toString(), requestId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        if (request.get().getStatus() != HelpRequestStatus.NEW) {
            String msg = (String.format("You can reject only opened requests. This request is %s", request.get().getStatus(),
                    requestId.toString()));
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }
        RejectedRequest rejectedRequest = new RejectedRequest(
                UUID.randomUUID(),
                godId,
                requestId
        ) ;
        rejectedRequestRepository.saveAndFlush(rejectedRequest);
        log.info("God {} rejected request {}", godId.toString(), requestId.toString());
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
}
