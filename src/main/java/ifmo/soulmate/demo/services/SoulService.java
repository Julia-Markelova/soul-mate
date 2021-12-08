package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.*;
import ifmo.soulmate.demo.entities.enums.*;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.HelpRequestDto;
import ifmo.soulmate.demo.models.LifeDto;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SoulService {
    @Autowired
    SoulRepository soulRepository;
    @Autowired
    ILifesRepository iLifesRepository;
    @Autowired
    ISoulRelativeRepository isoulRelativeRepository;
    @Autowired
    IMessageRepository iMessageRepository;
    @Autowired
    HelpRequestRepository helpRequestRepository;
    @Autowired
    ILifesRepository lifeRepository;
    @Autowired
    UserRepository userRepository;

    private static final Logger log = LogManager.getLogger(SoulService.class);

    private final Random random = new Random();

    public SoulDto getSoulByUserId(UUID userId) throws NonExistingEntityException {
        Optional<Soul> soul = soulRepository.getByUserId(userId);
        if (!soul.isPresent()) {
            String msg = (String.format("No soul found with userId: %s", userId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        log.info("Get soul by userId {}", userId);
        Soul unwrapped = soul.get();
        return new SoulDto(unwrapped.getId().toString(), unwrapped.getStatus(), "", unwrapped.getIsMentor());
    }

    private void notifyRelativesAboutSoulStatus(Soul updatedSoul) {
        List<UUID> relativesForNotify =
                isoulRelativeRepository.getRelativesWithAllowedNotificationsForSoulId(updatedSoul.getId());
        for (UUID relativeId : relativesForNotify) {
            Life relative = iLifesRepository.getById(relativeId);
            String msg_text = "Уважаемый(ая) " + relative.getSoulName() + " " + relative.getSoulSurname() +
                    ", Ваша родственная душа получила новый статус: " + updatedSoul.getStatus().toString();
            Message message = new Message(
                    UUID.randomUUID(), relativeId, msg_text, MessageStatus.NEW
            );
            iMessageRepository.saveAndFlush(message);
        }
    }

    private void notifyRelativesAboutSoulMentor(Soul updatedSoul) {
        List<UUID> relativesForNotify =
                isoulRelativeRepository.getRelativesWithAllowedNotificationsForSoulId(updatedSoul.getId());
        for (UUID relativeId : relativesForNotify) {
            Life relative = iLifesRepository.getById(relativeId);
            String mentor;
            if (updatedSoul.getIsMentor()) {
                mentor = "стала наствником";
            } else {
                mentor = "перестала быть наставником";
            }
            String msg_text = "Уважаемый(ая) " + relative.getSoulName() + " " + relative.getSoulSurname() +
                    ", Ваша родственная душа " + mentor + ".";
            Message message = new Message(
                    UUID.randomUUID(), relativeId, msg_text, MessageStatus.NEW
            );
            iMessageRepository.saveAndFlush(message);
        }
    }

    private String getRandomStatus(SoulStatus status) throws Exception {

        switch (status) {
            case BORN:
                return random.nextInt(100) + " лет";
            case DEAD:
                return "Умерла " + random.nextInt(100) + "лет назад";
            case LOST:
                return random.nextInt(2) == 0 ? "Ожидает помощи бога" : "Не пытается спастись";
            case UNBORN:
                return random.nextInt(2) == 0 ? "Проходит программу тренировки" : "Ожидает билет в жизнь";
            default:
                throw new Exception("Unknown status: " + status.toString());
        }
    }

    public List<SoulDto> getSouls() {
        List<Soul> souls = soulRepository.findAll();
        return souls
                .stream()
                .map(x -> {
                    try {
                        return new SoulDto(x.getId().toString(), x.getStatus(), getRandomStatus(x.getStatus()), x.getIsMentor());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public SoulDto updateSoulStatus(UUID soulId, SoulStatus status) throws NonExistingEntityException {
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            Soul unwrapped = soul.get();
            if (unwrapped.getStatus() != status) {
                unwrapped.setStatus(status);
                soulRepository.saveAndFlush(unwrapped);
                notifyRelativesAboutSoulStatus(unwrapped);
            }
            return new SoulDto(unwrapped.getId().toString(), unwrapped.getStatus(), "", unwrapped.getIsMentor());
        }
        String msg = (String.format("No soul found with id: %s", soulId.toString()));
        log.warn(msg);
        throw new NonExistingEntityException(msg);
    }

    public SoulDto updateSoulMentor(UUID soulId, Boolean isMentor) throws NonExistingEntityException {
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            Soul unwrapped = soul.get();
            if (unwrapped.getStatus() != SoulStatus.DEAD) {
                String msg = (String.format("Only DEAD souls can be mentors. Soul %s has status %s", soulId.toString(), unwrapped.getStatus()));
                log.warn(msg);
                throw new IllegalArgumentException(msg);
            }
            if (unwrapped.getIsMentor() != isMentor) {
                unwrapped.setIsMentor(isMentor);
                soulRepository.saveAndFlush(unwrapped);
                notifyRelativesAboutSoulMentor(unwrapped);
                UserRole role = (isMentor) ? UserRole.MENTOR : UserRole.SOUL;
                Optional<User> user = userRepository.findById(unwrapped.getUserId());
                if (user.isPresent()) {
                    user.get().setRole(role);
                    userRepository.saveAndFlush(user.get());
                }
            }
            return new SoulDto(unwrapped.getId().toString(), unwrapped.getStatus(), "", unwrapped.getIsMentor());
        }

        String msg = (String.format("No soul found with id: %s", soulId.toString()));
        log.warn(msg);
        throw new NonExistingEntityException(msg);

    }

    public HelpRequestDto createNewHelpRequestForGod(UUID soulId) throws NonExistingEntityException {
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            Soul unwrapped = soul.get();
            if (unwrapped.getStatus() == SoulStatus.LOST) {
                List<HelpRequest> newHelpRequests = helpRequestRepository.getByCreatedByAndStatusAndType(soulId, HelpRequestStatus.NEW, HelpRequestType.GOD);
                List<HelpRequest> acceptedHelpRequests = helpRequestRepository.getByCreatedByAndStatusAndType(soulId, HelpRequestStatus.ACCEPTED, HelpRequestType.GOD);
                if (newHelpRequests.size() > 0 || acceptedHelpRequests.size() > 0) {
                    String msg = (String.format("Soul %s has already opened requests for astral", soulId.toString()));
                    log.warn(msg);
                    throw new IllegalArgumentException(msg);
                }
                HelpRequest helpRequest = new HelpRequest( UUID.randomUUID(), HelpRequestStatus.NEW, soulId, HelpRequestType.GOD);
                helpRequest = helpRequestRepository.saveAndFlush(helpRequest);
                return new HelpRequestDto(helpRequest.getId().toString(), helpRequest.getCreatedBy().toString(), helpRequest.getStatus());
            } else {
                String msg = (String.format("Soul %s is not LOST", soulId.toString()));
                log.warn(msg);
                throw new IllegalArgumentException(msg);
            }
        } else {
            String msg = (String.format("No soul found with id: %s", soulId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
    }

    public HelpRequestDto createNewHelpRequestForMentor(UUID soulId) throws NonExistingEntityException {
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            Soul unwrapped = soul.get();
            if (unwrapped.getStatus() == SoulStatus.UNBORN) {
                List<HelpRequest> newHelpRequests = helpRequestRepository.getByCreatedByAndStatusAndType(soulId, HelpRequestStatus.NEW, HelpRequestType.MENTOR);
                List<HelpRequest> acceptedHelpRequests = helpRequestRepository.getByCreatedByAndStatusAndType(soulId, HelpRequestStatus.ACCEPTED, HelpRequestType.MENTOR);
                if (newHelpRequests.size() > 0 || acceptedHelpRequests.size() > 0) {
                    String msg = (String.format("Soul %s has already opened requests for life sparks", soulId.toString()));
                    log.warn(msg);
                    throw new IllegalArgumentException(msg);
                }
                HelpRequest helpRequest = new HelpRequest(UUID.randomUUID(), HelpRequestStatus.NEW, soulId, HelpRequestType.MENTOR);
                helpRequest = helpRequestRepository.saveAndFlush(helpRequest);
                return new HelpRequestDto(helpRequest.getId().toString(), helpRequest.getCreatedBy().toString(), helpRequest.getStatus());
            } else {
                String msg = (String.format("Soul %s is not UNBORN", soulId.toString()));
                log.warn(msg);
                throw new IllegalArgumentException(msg);
            }
        } else {
            String msg = (String.format("No soul found with id: %s", soulId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
    }

    public List<HelpRequestDto> getHelpRequestsBySoulId(UUID soulId, HelpRequestType type) {
        List<HelpRequest> requests = helpRequestRepository.getByCreatedByAndType(soulId, type);
        log.info("Get all requests for soul {}", soulId);
        return requests
                .stream()
                .map(x -> {
                    if (x.getAcceptedBy() == null) {
                        try {
                            return new HelpRequestDto(x.getId().toString(), x.getCreatedBy().toString(), x.getStatus());
                        } catch (Exception e) {
                            log.warn(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    try {
                        return new HelpRequestDto(x.getId().toString(), x.getCreatedBy().toString(), x.getAcceptedBy().toString(), x.getStatus());
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public List<LifeDto> getSoulLives(UUID soulId) {
        List<Life> lives = iLifesRepository.getLifeBySoulId(soulId);
        return lives
                .stream()
                .map(x -> {
                    try {
                        return new LifeDto(
                                x.getId().toString(),
                                x.getSoulId().toString(),
                                x.getSoulName(),
                                x.getSoulSurname(),
                                x.getDateOfBirth(),
                                x.getDateOfDeath()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
