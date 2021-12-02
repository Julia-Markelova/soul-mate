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

    @GetMapping("/notifications/{relativeId}")
    public ResponseEntity<List<MessageDto>> getNewMessages(@PathVariable String relativeId) {
        return ResponseEntity.ok(notificationService.getNewMessagesForRelative(UUID.fromString(relativeId)));
    }

    @GetMapping("/relatives/{relativeId}/subscriptions")
    public ResponseEntity toggleSubscribe(@PathVariable String relativeId, @RequestParam boolean enable) {
        if (enable) {
            notificationService.subscribe(UUID.fromString(relativeId));
        } else {
            notificationService.unsubscribe(UUID.fromString(relativeId));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/relatives/{relativeId}/subscriptions-status")
    public ResponseEntity<Boolean> getSubscriptionStatus(@PathVariable String relativeId) {
        return ResponseEntity.ok(notificationService.getSubscriptionStatus(UUID.fromString(relativeId)));
    }

    @PostMapping("/souls/{soulId}/create-help-request")
    @ApiOperation(value = "Создает заявку на выход из астрала",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> createHelpRequest(@RequestHeader String token, @PathVariable String soulId) throws NonExistingEntityException {
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        HelpRequestDto helpRequestDto;
        try {
            helpRequestDto = soulService.createNewHelpRequest(UUID.fromString(soulId));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @GetMapping("/souls/{soulId}/my-requests")
    @ApiOperation(value = "Получить список запросов на выход из астрала, которые созданы душой",
            notes = "Для запроса нужно быть авторизованным админом/душой",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getGodRequests(@RequestHeader String token, @PathVariable String soulId) {
        try {
            loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(soulService.getHelpRequestsBySoulId(UUID.fromString(soulId)));
    }

    @GetMapping("/souls/{soulId}/personal-program")
    @ApiOperation(value = "Получить информацию о персональной программе. " +
            "Если программы еще нет, то она будет создана автоматически.",
            notes = "Для запроса нужно быть авторизованной душой. Количество упражнений в каждой программе фиксированно." +
                    "Задается в контроллеле души. По умолчанию 5.",
            response = ResponseEntity.class)
    public ResponseEntity<PersonalProgramDto> getPersonalProgram(@RequestHeader String token, @PathVariable String soulId) {
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        Optional<PersonalProgramDto> personalProgram = personalProgramService.getPersonalProgramBySoulId(UUID.fromString(soulId));
        return personalProgram.map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.ok(personalProgramService.createPersonalProgram(UUID.fromString(soulId), numberOfExercisesInPersonalProgram)));
    }

    @GetMapping("/souls/get-by-userId/{userId}")
    @ApiOperation(value = "Получить информацию о душе по юзер-ид",
            notes = "Для запроса нужно быть авторизованным админом/душой",
            response = ResponseEntity.class)
    public ResponseEntity<SoulDto> getGodByUserId(@RequestHeader String token, @PathVariable String userId) {
        try {
            loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            return ResponseEntity.ok(soulService.getSoulByUserId(UUID.fromString(userId)));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
