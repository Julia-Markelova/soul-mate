package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.GodDto;
import ifmo.soulmate.demo.models.HelpRequestDto;
import ifmo.soulmate.demo.services.GodService;
import ifmo.soulmate.demo.services.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SessionAttributes({"userId"})
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class GodController {

    @Autowired
    GodService godService;

    @Autowired
    LoginService loginService;

    @GetMapping("/gods/{godId}")
    @ApiOperation(value = "Получить информацию о боге",
            notes = "Для запроса нужно быть авторизованным админом/богом",
            response = ResponseEntity.class)
    public ResponseEntity<GodDto> getGodById(@RequestHeader String token, @PathVariable String godId) {
        try {
            loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.GOD));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            return ResponseEntity.ok(godService.getGodById(UUID.fromString(godId)));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/gods/get-by-userId/{userId}")
    @ApiOperation(value = "Получить информацию о боге по юзер-ид",
            notes = "Для запроса нужно быть авторизованным админом/богом",
            response = ResponseEntity.class)
    public ResponseEntity<GodDto> getGodByUserId(@RequestHeader String token, @PathVariable String userId) {
        try {
            loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.GOD));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            return ResponseEntity.ok(godService.getGodByUserId(UUID.fromString(userId)));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/gods/{godId}/new-requests")
    @ApiOperation(value = "Получить список новых запросов на выход из астрала",
            notes = "Для запроса нужно быть авторизованным админом/богом",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getNewRequests(@RequestHeader String token, @PathVariable String godId) {
        try {
            loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.GOD));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(godService.getNewHelpRequests(UUID.fromString(godId)));
    }

    @PutMapping("/gods/{godId}/accept-request/{requestId}")
    @ApiOperation(value = "Принять запрос о помощи",
            notes = "Для запроса нужно быть авторизованным богом",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> acceptRequest(@RequestHeader String token, @PathVariable String godId, @PathVariable String requestId) {
        HelpRequestDto helpRequestDto;
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.GOD));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            helpRequestDto = godService.updateStatusForRequest(UUID.fromString(godId), UUID.fromString(requestId), HelpRequestStatus.ACCEPTED);
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @PutMapping("/gods/{godId}/finish-request/{requestId}")
    @ApiOperation(value = "Завершить заявку о помощи",
            notes = "Для запроса нужно быть авторизованным богом",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> finishRequest(@RequestHeader String token, @PathVariable String godId, @PathVariable String requestId) {
        HelpRequestDto helpRequestDto;
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.GOD));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            helpRequestDto = godService.updateStatusForRequest(UUID.fromString(godId), UUID.fromString(requestId), HelpRequestStatus.FINISHED);
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @GetMapping("/gods/{godId}/my-requests")
    @ApiOperation(value = "Получить список запросов на выход из астрала, которые выполнил/выполняет бог",
            notes = "Для запроса нужно быть авторизованным админом/богом",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getGodRequests(@RequestHeader String token, @PathVariable String godId) {
        try {
            loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.ADMIN, UserRole.GOD));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(godService.getHelpRequestsByGodId(UUID.fromString(godId)));
    }
}
