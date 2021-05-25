package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.entities.SoulStatus;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.repositories.SoulRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SoulService {
    @Autowired
    SoulRepository soulRepository;
    private final Random random = new Random();

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
            }
        }
    }
}
