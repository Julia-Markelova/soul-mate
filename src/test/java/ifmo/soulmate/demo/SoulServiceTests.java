package ifmo.soulmate.demo;

import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.repositories.HelpRequestRepository;
import ifmo.soulmate.demo.repositories.SoulRepository;
import ifmo.soulmate.demo.repositories.UserRepository;
import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.services.SoulService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SoulServiceTests {

    @Mock
    SoulRepository soulRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    HelpRequestRepository helpRequestRepository;
    @InjectMocks
    SoulService soulService;

    @Test
    public void whenGetSoulThenReturnSoulDto() throws NonExistingEntityException {
        UUID userId = UUID.randomUUID();
        UUID soulId = UUID.randomUUID();
        SoulStatus soulStatus = SoulStatus.UNBORN;
        boolean isMentor = false;
        when(soulRepository.getByUserId(userId)).thenReturn(Optional.of(new Soul(soulId, soulStatus, userId, isMentor)));
        SoulDto soulDto = soulService.getSoulByUserId(userId);
        assertThat(soulDto.getId()).isEqualTo(soulId.toString());
    }

    @Test
    public void whenGetSoulsThenReturnSoulDtos() {
        List<Soul> souls = new ArrayList<Soul>();
        souls.add(new Soul(UUID.randomUUID(), SoulStatus.BORN, UUID.randomUUID(), false));
        souls.add(new Soul(UUID.randomUUID(), SoulStatus.BORN, UUID.randomUUID(), false));
        when(soulRepository.findAll()).thenReturn(souls);
        List<SoulDto> soulDtos = soulService.getSouls();
        assertThat(soulDtos.size()).isEqualTo(souls.size());
    }

    @Test
    public void whenUpdateStatusThenReturnUpdatedSoulDto() throws NonExistingEntityException {
        UUID soulId = UUID.randomUUID();
        SoulStatus status = SoulStatus.BORN;
        Soul soul  = new Soul(soulId, status, UUID.randomUUID(), false);
        when(soulRepository.findById(soulId)).thenReturn(Optional.of(soul));
        SoulDto soulDto = soulService.updateSoulStatus(soulId, status);
        assertThat(soulDto.getStatus()).isEqualTo(status);
    }
}
