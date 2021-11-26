package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.Message;
import ifmo.soulmate.demo.entities.enums.MessageStatus;
import ifmo.soulmate.demo.entities.SoulRelative;
import ifmo.soulmate.demo.models.MessageDto;
import ifmo.soulmate.demo.repositories.IMessageRepository;
import ifmo.soulmate.demo.repositories.ISoulRelativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    IMessageRepository iMessageRepository;

    @Autowired
    ISoulRelativeRepository iSoulRelativeRepository;

    public List<MessageDto> getNewMessagesForRelative(UUID relativeId) {
        List<Message> messages = iMessageRepository.getMessagesByStatusAndRelativeId(MessageStatus.NEW, relativeId);
        for (Message m: messages
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

    public void setDeliveredStatusForMessage(Message message) {
        message.setStatus(MessageStatus.DELIVERED);
        iMessageRepository.saveAndFlush(message);
    }

    public List<SoulRelative> getAllSubscriptionsForRelative(UUID relativeId) {
        return iSoulRelativeRepository.getSoulRelativeByRelativeIdAndNotifyRelativeAboutSoul(relativeId, true);
    }

    public void unsubscribe(UUID relativeId) {
        List<SoulRelative> soulRelatives = iSoulRelativeRepository.getSoulRelativeByRelativeId(relativeId);
        for (SoulRelative s: soulRelatives
        ) {
            s.setNotifyRelativeAboutSoul(false);
            iSoulRelativeRepository.saveAndFlush(s);
        }
    }

    public void subscribe(UUID relativeId) {
        List<SoulRelative> soulRelatives = iSoulRelativeRepository.getSoulRelativeByRelativeId(relativeId);
        for (SoulRelative s: soulRelatives
             ) {
            s.setNotifyRelativeAboutSoul(true);
            iSoulRelativeRepository.saveAndFlush(s);
        }
    }

    public boolean getSubscriptionStatus(UUID relativeId) {
        List<SoulRelative> soulRelatives = iSoulRelativeRepository.getSoulRelativeByRelativeId(relativeId);
        return soulRelatives.stream().allMatch(SoulRelative::getNotifyRelativeAboutSoul);
    }
}
