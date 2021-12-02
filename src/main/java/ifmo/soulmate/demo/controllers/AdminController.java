package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.models.SystemModeDto;
import ifmo.soulmate.demo.services.AdminService;
import ifmo.soulmate.demo.services.LoginService;
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
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private LoginService loginService;

    @Autowired
    SoulService soulService;

    @GetMapping("/souls")
    @ApiOperation(value = "Получить список душ",
            notes = "Для запроса нужно быть авторизованным админом",
            response = ResponseEntity.class)
    public ResponseEntity<List<SoulDto>> getAllSouls(@RequestHeader String token) {
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.ADMIN));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(soulService.getSouls());
    }

    @RequestMapping(value = "/getAllModes", method = RequestMethod.GET)
    @ApiOperation(value = "Получить информацию о режимах в системе",
            notes = "Для запроса нужно быть авторизованным админом",
            response = ResponseEntity.class)
    public ResponseEntity<List<SystemModeDto>> getAllModes(@RequestHeader String token) {
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.ADMIN));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(adminService.getAllModes());
    }

    @RequestMapping(value = "/changeMode", method = RequestMethod.GET)
    @ApiOperation(value = "Изменить режим системы: поставить ручной или автоматический",
            notes = "Для запроса нужно быть авторизованным админом.")
    public ResponseEntity<String> changeMode(@RequestHeader String token,
                                             @RequestParam UUID modeId, @RequestParam boolean isManualMode) {
        try {
            loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.ADMIN));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        adminService.updateMode(modeId, isManualMode);
        return ResponseEntity.ok("Success changed mode");
    }
}
