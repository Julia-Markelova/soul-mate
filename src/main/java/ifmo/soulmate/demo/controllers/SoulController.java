package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.LifeTicket;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.*;
import ifmo.soulmate.demo.services.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class SoulController {

    @Autowired
    SoulService soulService;

    @Autowired
    LifeTicketService lifeTicketService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    LoginService loginService;

    @Autowired
    PersonalProgramService personalProgramService;

    private final int numberOfExercisesInPersonalProgram = 5;

    @GetMapping("/souls/{id}/life-tickets")
    public ResponseEntity<LifeTicket> getLifeTicket(@PathVariable String id) {
        return ResponseEntity.ok(lifeTicketService.getNotUsedLifeTicket(UUID.fromString(id)));
    }

    @PutMapping("/souls/{soulId}/life-tickets/{ticketId}")
    public ResponseEntity receiveLifeTicket(@PathVariable String soulId, @PathVariable String ticketId) {
        lifeTicketService.receiveLifeTicket(UUID.fromString(soulId), UUID.fromString(ticketId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/souls/{soulId}/update/status/{status}")
    public ResponseEntity updateSoulStatus(@PathVariable String soulId, @PathVariable String status) {
        soulService.updateSoulStatus(UUID.fromString(soulId), SoulStatus.valueOf(status));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/souls/{soulId}/update/mentor/{isMentor}")
    public ResponseEntity updateSoulMentor(@PathVariable String soulId, @PathVariable Boolean isMentor) {
        soulService.updateSoulMentor(UUID.fromString(soulId), isMentor);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/souls/create-help-request")
    @ApiOperation(value = "Создает заявку на выход из астрала",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> createHelpRequest(@RequestHeader("soul-token") String token) throws NonExistingEntityException {
        UserDto userDto;
        SoulDto soulDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        HelpRequestDto helpRequestDto;
        try {
            soulDto = soulService.getSoulByUserId(UUID.fromString(userDto.getId()));
            helpRequestDto = soulService.createNewHelpRequest(UUID.fromString(soulDto.getId()));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @GetMapping("/souls/my-requests")
    @ApiOperation(value = "Получить список запросов на выход из астрала, которые созданы душой",
            notes = "Для запроса нужно быть авторизованным админом/душой",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getGodRequests(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        SoulDto soulDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            soulDto = soulService.getSoulByUserId(UUID.fromString(userDto.getId()));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(soulService.getHelpRequestsBySoulId(UUID.fromString(soulDto.getId())));
    }

    @GetMapping("/souls/personal-program")
    @ApiOperation(value = "Получить информацию о персональной программе. " +
            "Если программы еще нет, то она будет создана автоматически.",
            notes = "Для запроса нужно быть авторизованной душой. Количество упражнений в каждой программе фиксированно." +
                    "Задается в контроллеле души. По умолчанию 5.",
            response = ResponseEntity.class)
    public ResponseEntity<PersonalProgramDto> getPersonalProgram(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        SoulDto soulDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            soulDto = soulService.getSoulByUserId(UUID.fromString(userDto.getId()));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        Optional<PersonalProgramDto> personalProgram = personalProgramService.getPersonalProgramBySoulId(UUID.fromString(soulDto.getId()));
        return personalProgram.map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.ok(personalProgramService.createPersonalProgram(UUID.fromString(soulDto.getId()), numberOfExercisesInPersonalProgram)));
    }
}
