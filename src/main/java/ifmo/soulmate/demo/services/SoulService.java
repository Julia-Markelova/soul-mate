package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.controllers.LoginController;
import ifmo.soulmate.demo.entities.*;
import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.MessageStatus;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.HelpRequestDto;
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

    private static final Logger log = LogManager.getLogger(LoginController.class);

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
        return new SoulDto(unwrapped.getId().toString(), unwrapped.getStatus(), "");
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
                        return new SoulDto(x.getId().toString(), x.getStatus(), getRandomStatus(x.getStatus()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void updateSoulStatus(UUID soulId, SoulStatus status) {
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            Soul unwrapped = soul.get();
            if (unwrapped.getStatus() != status) {
                unwrapped.setStatus(status);
                soulRepository.saveAndFlush(unwrapped);
                notifyRelativesAboutSoulStatus(unwrapped);
            }
        }
    }

    public void updateSoulMentor(UUID soulId, Boolean isMentor) {
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            Soul unwrapped = soul.get();
            if (unwrapped.getIsMentor() != isMentor) {
                unwrapped.setIsMentor(isMentor);
                soulRepository.saveAndFlush(unwrapped);
                notifyRelativesAboutSoulMentor(unwrapped);
            }
        }
    }

    public HelpRequestDto createNewHelpRequest(UUID soulId) throws NonExistingEntityException {
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            Soul unwrapped = soul.get();
            if (unwrapped.getStatus() == SoulStatus.LOST) {
                HelpRequest helpRequest = new HelpRequest(UUID.randomUUID(), HelpRequestStatus.NEW, soulId);
                helpRequestRepository.saveAndFlush(helpRequest);
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

    public List<HelpRequestDto> getHelpRequestsBySoulId(UUID soulId) {
        List<HelpRequest> requests = helpRequestRepository.getByCreatedBy(soulId);
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
}
