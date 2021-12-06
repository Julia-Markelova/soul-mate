package ifmo.soulmate.demo;

import ifmo.soulmate.demo.entities.*;
import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.HelpRequestType;
import ifmo.soulmate.demo.entities.enums.MessageStatus;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.*;
import ifmo.soulmate.demo.repositories.*;
import ifmo.soulmate.demo.services.GodService;
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
public class GodServiceTests {

    @Mock
    GodRepository godRepository;
    @Mock
    HelpRequestRepository helpRequestRepository;
    @InjectMocks
    GodService godService;


    @Test
    public void whenGetGodByUserIdReturnGodDto() throws NonExistingEntityException {
        UUID userId = UUID.randomUUID();
        when(godRepository.getByUserId(userId)).thenReturn(Optional.of(new God(UUID.randomUUID(), userId, "")));
        GodDto godDto = godService.getGodByUserId(userId);
        assertThat(godDto.getUserId()).isEqualTo(userId.toString());
    }

    @Test
    public void whenGetHelpRequestsByGodIdReturnHelpRequestDto() {
        UUID godId = UUID.randomUUID();
        List<HelpRequest> helpRequests = new ArrayList<>();
        helpRequests.add(new HelpRequest(UUID.randomUUID(), HelpRequestStatus.ACCEPTED, UUID.randomUUID(), godId, HelpRequestType.GOD));
        helpRequests.add(new HelpRequest(UUID.randomUUID(), HelpRequestStatus.ACCEPTED, UUID.randomUUID(), godId, HelpRequestType.GOD));
        helpRequests.add(new HelpRequest(UUID.randomUUID(), HelpRequestStatus.ACCEPTED, UUID.randomUUID(), godId, HelpRequestType.GOD));
        when(helpRequestRepository.getByAcceptedBy(godId)).thenReturn(helpRequests);
        List<HelpRequestDto> requestDtos = godService.getHelpRequestsByGodId(godId);
        assertThat(requestDtos.size()).isEqualTo(helpRequests.size());
    }
}