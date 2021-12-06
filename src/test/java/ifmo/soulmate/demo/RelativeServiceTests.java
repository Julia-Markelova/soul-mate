package ifmo.soulmate.demo;

import ifmo.soulmate.demo.entities.Life;
import ifmo.soulmate.demo.entities.Message;
import ifmo.soulmate.demo.entities.SoulRelative;
import ifmo.soulmate.demo.entities.enums.MessageStatus;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.LifeDto;
import ifmo.soulmate.demo.models.MessageDto;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.models.SubscriptionDto;
import ifmo.soulmate.demo.repositories.*;
import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.services.RelativeService;
import ifmo.soulmate.demo.services.SoulService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelativeServiceTests {

    @Mock
    ILifesRepository livesRepository;
    @Mock
    IMessageRepository messageRepository;
    @Mock
    ISoulRelativeRepository soulRelativeRepository;
    @Mock
    SoulRepository soulRepository;
    @InjectMocks
    RelativeService relativeService;


    @Test
    public void whenGetRelativeIdThenReturnRelativeDto() throws NonExistingEntityException {
        UUID relativeId = UUID.randomUUID();
        UUID soulId = UUID.randomUUID();
        Date dateOfBirth = new Date(12345678);
        Date dateOfDeath = new Date(23456789);
        when(livesRepository.getLifeBySoulIdAndDateOfDeathIsNull(soulId))
                .thenReturn(Optional.of(new Life(
                        relativeId, soulId, dateOfBirth, dateOfDeath, "John", "Smith", 0
                )));
        LifeDto relativeDto = relativeService.getRelativeBySoulId(soulId);
        assertThat(relativeDto.getSoulId()).isEqualTo(soulId.toString());
    }

    @Test
    public void whenGetMessagesThenReturnMessagesDtos() {
        UUID relativeId = UUID.randomUUID();
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(UUID.randomUUID(), UUID.randomUUID(), "First message", MessageStatus.NEW));
        messages.add(new Message(UUID.randomUUID(), UUID.randomUUID(), "Second message", MessageStatus.NEW));
        when(messageRepository.getMessagesByStatusAndRelativeId(MessageStatus.NEW, relativeId)).thenReturn(messages);
        List<MessageDto> messageDtos = relativeService.getNewMessagesForRelative(relativeId);
        assertThat(messageDtos.size()).isEqualTo(messages.size());
    }

    @Test
    public void whenGetSubscriptionsThenReturnSubscriptionDtos() {
        UUID rootRelative = UUID.randomUUID();
        UUID firstRelative = UUID.randomUUID();
        UUID secondRelative = UUID.randomUUID();
        UUID thirdRelative = UUID.randomUUID();
        List<SoulRelative> relatives = new ArrayList<>();
        relatives.add(new SoulRelative(UUID.randomUUID(), firstRelative, rootRelative, true));
        relatives.add(new SoulRelative(UUID.randomUUID(), secondRelative, rootRelative, true));
        relatives.add(new SoulRelative(UUID.randomUUID(), thirdRelative, rootRelative, true));
        when(soulRelativeRepository.getSoulRelativeByRelativeId(rootRelative)).thenReturn(relatives);
        when(soulRepository.findById(firstRelative)).
                thenReturn(Optional.of(new Soul(UUID.randomUUID(), SoulStatus.BORN, UUID.randomUUID(), false)));
        when(soulRepository.findById(secondRelative)).
                thenReturn(Optional.of(new Soul(UUID.randomUUID(), SoulStatus.BORN, UUID.randomUUID(), false)));
        when(soulRepository.findById(thirdRelative)).
                thenReturn(Optional.of(new Soul(UUID.randomUUID(), SoulStatus.BORN, UUID.randomUUID(), false)));
        List<SubscriptionDto> subscriptionDtos = relativeService.getSubscriptions(rootRelative);
        assertThat(subscriptionDtos.size()).isEqualTo(relatives.size());

    }
}