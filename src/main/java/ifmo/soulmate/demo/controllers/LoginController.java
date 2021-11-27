package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.services.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@SessionAttributes({"userId"})
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    private LoginService loginService;

    private final String attribute = "userId";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "Вход в систему", notes = "Проставляет в сессию userId", response = ResponseEntity.class)
    public ResponseEntity<UserDto> login(HttpSession session, @RequestParam String login, @RequestParam String password) {
        try {
            UserDto user = loginService.loginUser(login, password);
            session.setAttribute(attribute, user.getId());
            return ResponseEntity.ok(user);

        } catch (AuthException ex) {
            return new ResponseEntity("Wrong login or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ApiOperation(value = "Выход из системы", notes = "Если в сессии есть userId, то удаляет его. Иначе - ошибка 401")
    public ResponseEntity logout(HttpSession session) {
        String userId = (String) session.getAttribute(attribute);
        if (userId == null) {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        session.removeAttribute(attribute);
        session.invalidate();
        return ResponseEntity.ok("Success logout");
    }
}