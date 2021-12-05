package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.Life;
import ifmo.soulmate.demo.entities.Message;
import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.entities.SoulRelative;
import ifmo.soulmate.demo.entities.enums.MessageStatus;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.LifeDto;
import ifmo.soulmate.demo.models.MessageDto;
import ifmo.soulmate.demo.models.SubscriptionDto;
import ifmo.soulmate.demo.repositories.ILifesRepository;
import ifmo.soulmate.demo.repositories.IMessageRepository;
import ifmo.soulmate.demo.repositories.ISoulRelativeRepository;
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

@Service
public class RelativeService {

    @Autowired
    IMessageRepository iMessageRepository;
    @Autowired
    ISoulRelativeRepository iSoulRelativeRepository;
    @Autowired
    SoulRepository soulRepository;
    @Autowired
    ILifesRepository lifeRepository;
    private static final Logger log = LogManager.getLogger(RelativeService.class);

    public List<MessageDto> getNewMessagesForRelative(UUID relativeId) {
        List<Message> messages = iMessageRepository.getMessagesByStatusAndRelativeId(MessageStatus.NEW, relativeId);
        for (Message m : messages
        ) {
            setDeliveredStatusForMessage(m);
        }
        return messages
                .stream()
                .map(x -> {
                    try {
                        return new MessageDto(x.getId().toString(), x.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void setDeliveredStatusForMessage(Message message) {
        message.setStatus(MessageStatus.DELIVERED);
        iMessageRepository.saveAndFlush(message);
    }

    public LifeDto getRelativeBySoulId(UUID soulId) throws NonExistingEntityException {
        Optional<Life> life = lifeRepository.getLifeBySoulIdAndDateOfDeathIsNull(soulId);
        if (!life.isPresent()) {
            String msg = (String.format("No relative found for soul id: %s", soulId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        log.info("Get relative by soulId {}", soulId);
        Life unwrapped = life.get();
        return new LifeDto(
                unwrapped.getId().toString(),
                unwrapped.getSoulId().toString(),
                unwrapped.getSoulName(),
                unwrapped.getSoulSurname(),
                unwrapped.getDateOfBirth(),
                unwrapped.getDateOfDeath()
        );
    }

    public List<SubscriptionDto> getSubscriptions(UUID relativeId) {
        List<SoulRelative> soulRelatives = iSoulRelativeRepository.getSoulRelativeByRelativeId(relativeId);
        return soulRelatives
                .stream()
                .map(x -> {
                    try {
                        Optional<Soul> soul = soulRepository.findById(x.getSoulId());
                        if (soul.isPresent()) {
                            return new SubscriptionDto(
                                    x.getId().toString(),
                                    x.getRelativeId().toString(),
                                    x.getSoulId().toString(),
                                    soul.get().getStatus(),
                                    soul.get().getIsMentor(),
                                    x.getNotifyRelativeAboutSoul()
                            );
                        }
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public SubscriptionDto editSubscription(UUID subscriptionId, boolean enable) throws NonExistingEntityException {
        Optional<SoulRelative> subscription = iSoulRelativeRepository.findById(subscriptionId);
        if (subscription.isPresent()) {
            SoulRelative unwrapped = subscription.get();
            unwrapped.setNotifyRelativeAboutSoul(enable);
            iSoulRelativeRepository.saveAndFlush(unwrapped);
            Optional<Soul> soul = soulRepository.findById(unwrapped.getSoulId());
            if (soul.isPresent()) {
                return new SubscriptionDto(
                        unwrapped.getId().toString(),
                        unwrapped.getRelativeId().toString(),
                        unwrapped.getSoulId().toString(),
                        soul.get().getStatus(),
                        soul.get().getIsMentor(),
                        unwrapped.getNotifyRelativeAboutSoul()
                );
            }
        }
        String msg = String.format("No subscription found for id %s", subscriptionId.toString());
        log.warn(msg);
        throw new NonExistingEntityException(msg);
    }

}
