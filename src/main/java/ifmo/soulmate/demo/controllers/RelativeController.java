package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.MainApiException;
import ifmo.soulmate.demo.models.LifeDto;
import ifmo.soulmate.demo.models.MessageDto;
import ifmo.soulmate.demo.models.SubscriptionDto;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.services.LifeTicketService;
import ifmo.soulmate.demo.services.LoginService;
import ifmo.soulmate.demo.services.RelativeService;
import ifmo.soulmate.demo.services.SoulService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RelativeController {
    @Autowired
    SoulService soulService;
    @Autowired
    LifeTicketService lifeTicketService;
    @Autowired
    RelativeService relativeService;
    @Autowired
    LoginService loginService;

    @GetMapping("/relatives/profile")
    @ApiOperation(value = "Получить информацию о родственнике",
            notes = "Для запроса нужно быть авторизованным родственником",
            response = ResponseEntity.class)
    public ResponseEntity<LifeDto> getRelativeBySoulId(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.RELATIVE));
            return ResponseEntity.ok(relativeService.getRelativeBySoulId(UUID.fromString(userDto.getRoleId())));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
    }

    @GetMapping("/relatives/get-new-messages")
    @ApiOperation(value = "Получить новые сообщения для родсвтенника",
            notes = "Для запроса нужно быть авторизованным родственником",
            response = ResponseEntity.class)
    public ResponseEntity<List<MessageDto>> getNewMessages(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        LifeDto lifeDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.RELATIVE));
            lifeDto = relativeService.getRelativeBySoulId(UUID.fromString(userDto.getRoleId()));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(relativeService.getNewMessagesForRelative(UUID.fromString(lifeDto.getId())));
    }

    @GetMapping("/relatives/toggle-subscriptions")
    @ApiOperation(value = "Управлять конкретной подпиской (вкл/выкл)",
            notes = "Для запроса нужно быть авторизованным родственником",
            response = ResponseEntity.class)
    public ResponseEntity<SubscriptionDto> toggleSubscribe(@RequestHeader("soul-token") String token,
                                                           @RequestParam String subscriptionId,
                                                           @RequestParam boolean enable) {
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.RELATIVE));
            return ResponseEntity.ok(relativeService.editSubscription(UUID.fromString(subscriptionId), enable));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
    }

    @GetMapping("/relatives/get-subscriptions")
    @ApiOperation(value = "Получить информацию о своих подписках",
            notes = "Для запроса нужно быть авторизованным родственником",
            response = ResponseEntity.class)
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        LifeDto lifeDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.RELATIVE));
            lifeDto = relativeService.getRelativeBySoulId(UUID.fromString(userDto.getRoleId()));
            return ResponseEntity.ok(relativeService.getSubscriptions(UUID.fromString(lifeDto.getId())));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
    }
}
