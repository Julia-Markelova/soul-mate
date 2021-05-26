package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.*;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.repositories.ILifesRepository;
import ifmo.soulmate.demo.repositories.IMessageRepository;
import ifmo.soulmate.demo.repositories.ISoulRelativeRepository;
import ifmo.soulmate.demo.repositories.SoulRepository;
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

    private final Random random = new Random();

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
            if (updatedSoul.getIs_mentor()) {
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
            if (unwrapped.getIs_mentor() != isMentor) {
                unwrapped.setIs_mentor(isMentor);
                soulRepository.saveAndFlush(unwrapped);
                notifyRelativesAboutSoulMentor(unwrapped);
            }
        }
    }
}
