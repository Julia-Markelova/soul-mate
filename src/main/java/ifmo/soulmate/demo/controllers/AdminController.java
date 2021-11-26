package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.models.SystemModeDto;
import ifmo.soulmate.demo.services.AdminService;
import ifmo.soulmate.demo.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SessionAttributes({"userId"})
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/getAllModes", method = RequestMethod.GET)
    public ResponseEntity<List<SystemModeDto>> getAllModes(Model model, HttpSession session) {
        try {
            loginService.authoriseAndCheckPermission(session, Collections.singletonList(UserRole.ADMIN));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(adminService.getAllModes());
    }

    @RequestMapping(value = "/changeMode", method = RequestMethod.GET)
    public ResponseEntity changeMode(Model model, HttpSession session,
                                     @RequestParam UUID modeId, @RequestParam boolean isManualMode) {
        try {
            loginService.authoriseAndCheckPermission(session, Collections.singletonList(UserRole.ADMIN));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        adminService.updateMode(modeId, isManualMode);
        return ResponseEntity.ok("Success changed mode");
    }
}
