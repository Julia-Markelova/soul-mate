package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.MainApiException;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.HelpRequestDto;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.services.LifeTicketService;
import ifmo.soulmate.demo.services.LoginService;
import ifmo.soulmate.demo.services.MentorService;
import ifmo.soulmate.demo.services.SoulService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class MentorController {

    @Autowired
    LoginService loginService;
    @Autowired
    MentorService mentorService;
    @Autowired
    SoulService soulService;
    @Autowired
    LifeTicketService lifeTicketService;

    @GetMapping("/mentors/open-requests")
    @ApiOperation(value = "Получить список открытых запросов на получение искры жизни",
            notes = "Для запроса нужно быть авторизованным ментором",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getOpenRequests(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.MENTOR));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(mentorService.getOpenHelpRequests(UUID.fromString(userDto.getRoleId())));
    }

    @PutMapping("/mentors/accept-request/{requestId}")
    @ApiOperation(value = "Принять запрос о помощи для получения билета в жизнь",
            notes = "Для запроса нужно быть авторизованным ментором",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> acceptRequest(@RequestHeader("soul-token") String token, @PathVariable String requestId) {
        HelpRequestDto helpRequestDto;
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.MENTOR));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            helpRequestDto = mentorService.updateStatusForRequest(UUID.fromString(userDto.getRoleId()), UUID.fromString(requestId), HelpRequestStatus.ACCEPTED);
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @PutMapping("/mentors/finish-request/{requestId}")
    @ApiOperation(value = "Завершить заявку о помощи в поиске искры жизни",
            notes = "Создает для души искру жизни. Для запроса нужно быть авторизованным ментором",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> finishRequest(@RequestHeader("soul-token") String token, @PathVariable String requestId) {
        HelpRequestDto helpRequestDto;
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.MENTOR));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            helpRequestDto = mentorService.updateStatusForRequest(UUID.fromString(userDto.getRoleId()), UUID.fromString(requestId), HelpRequestStatus.FINISHED);
            lifeTicketService.createLifeSpark(UUID.fromString(helpRequestDto.getCreatedBy()), UUID.fromString(helpRequestDto.getAcceptedBy()));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }


    @PutMapping("/mentors/reject-request/{requestId}")
    @ApiOperation(value = "Отклонить заявку о помощи",
            notes = "Для запроса нужно быть авторизованным ментором",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> rejectRequest(@RequestHeader("soul-token") String token, @PathVariable String requestId) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.MENTOR));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            mentorService.rejectRequest(UUID.fromString(userDto.getRoleId()), UUID.fromString(requestId));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mentors/my-requests")
    @ApiOperation(value = "Получить список запросов на поиск искры жизни, которые выполнил/выполняет ментор",
            notes = "Для запроса нужно быть авторизованным ментором",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getMentorRequests(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.MENTOR));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(mentorService.getHelpRequestsByMentorId(UUID.fromString(userDto.getRoleId())));
    }
}
