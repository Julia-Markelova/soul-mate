package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.exceptions.MainApiException;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.services.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "Вход в систему", notes = "Возвращает UserDto", response = ResponseEntity.class)
    public ResponseEntity<UserDto> login(@RequestParam String login, @RequestParam String password) {
        try {
            UserDto user = loginService.loginUser(login, password);
            return ResponseEntity.ok(user);

        } catch (MainApiException ex) {
            return new ResponseEntity("Wrong login or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ApiOperation(value = "Выход из системы", notes = "Ничего не делает. Токен с фронта лучше удалить")
    public ResponseEntity logout(@RequestHeader("soul-token") String token) {
        try {
            loginService.authoriseUser(token);
            return ResponseEntity.ok("Success logout");

        } catch (AuthException | NonExistingEntityException ex) {
            return new ResponseEntity("Wrong login or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ApiOperation(value = "Получить юзера по токену")
    public ResponseEntity<UserDto> getUserByToken(@RequestHeader("soul-token") String token) {
        try {
            UserDto user = loginService.authoriseUser(token);
            return ResponseEntity.ok(user);

        } catch (AuthException | NonExistingEntityException ex) {
            return new ResponseEntity("Authorization error", HttpStatus.UNAUTHORIZED);
        }
    }

}