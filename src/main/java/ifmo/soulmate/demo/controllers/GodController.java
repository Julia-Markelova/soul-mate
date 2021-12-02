package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.MainApiException;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.GodDto;
import ifmo.soulmate.demo.models.HelpRequestDto;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.services.GodService;
import ifmo.soulmate.demo.services.LoginService;
import ifmo.soulmate.demo.services.SoulService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class GodController {

    @Autowired
    GodService godService;

    @Autowired
    LoginService loginService;

    @Autowired
    SoulService soulService;

    @GetMapping("/gods/profile")
    @ApiOperation(value = "Получить информацию о боге",
            notes = "Для запроса нужно быть авторизованным админом/богом",
            response = ResponseEntity.class)
    public ResponseEntity<GodDto> getGodById(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.GOD));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            return ResponseEntity.ok(godService.getGodByUserId(UUID.fromString(userDto.getId())));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/gods/open-requests")
    @ApiOperation(value = "Получить список открытых запросов на выход из астрала",
            notes = "Для запроса нужно быть авторизованным админом/богом",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getOpenRequests(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.GOD));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(godService.getOpenHelpRequests(UUID.fromString(userDto.getRoleId())));
    }

    @PutMapping("/gods/accept-request/{requestId}")
    @ApiOperation(value = "Принять запрос о помощи",
            notes = "Для запроса нужно быть авторизованным богом",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> acceptRequest(@RequestHeader("soul-token") String token, @PathVariable String requestId) {
        HelpRequestDto helpRequestDto;
        UserDto userDto;
        try {
            userDto =loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.GOD));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            helpRequestDto = godService.updateStatusForRequest(UUID.fromString(userDto.getRoleId()), UUID.fromString(requestId), HelpRequestStatus.ACCEPTED);
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @PutMapping("/gods/finish-request/{requestId}")
    @ApiOperation(value = "Завершить заявку о помощи",
            notes = "Проставляет душе статус DEAD. Для запроса нужно быть авторизованным богом",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> finishRequest(@RequestHeader("soul-token") String token, @PathVariable String requestId) {
        HelpRequestDto helpRequestDto;
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.GOD));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            helpRequestDto = godService.updateStatusForRequest(UUID.fromString(userDto.getRoleId()), UUID.fromString(requestId), HelpRequestStatus.FINISHED);
            soulService.updateSoulStatus(UUID.fromString(helpRequestDto.getCreatedBy()), SoulStatus.DEAD);
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @GetMapping("/gods/my-requests")
    @ApiOperation(value = "Получить список запросов на выход из астрала, которые выполнил/выполняет бог",
            notes = "Для запроса нужно быть авторизованным админом/богом",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getGodRequests(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.GOD));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(godService.getHelpRequestsByGodId(UUID.fromString(userDto.getRoleId())));
    }
}
