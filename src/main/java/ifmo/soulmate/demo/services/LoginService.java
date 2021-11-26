package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.controllers.LoginController;
import ifmo.soulmate.demo.entities.User;
import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LogManager.getLogger(LoginController.class);

    public UserDto loginUser(String login, String password) throws AuthException {
        Optional<User> user = userRepository.getUserByLoginAndPassword(login, password);
        if (!user.isPresent()) {
            log.warn("Authentication failed: login = {}, password = {}", login, password);
            throw new AuthException("Wrong username or password", HttpStatus.UNAUTHORIZED);
        }
        log.info("Authentication success: user {} logged in", user.get().getId());
        return new UserDto((user.get().getId()).toString(), user.get().getRole(), user.get().getLogin());
    }

    public UserDto authoriseUser(HttpSession session) throws AuthException {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            throw new AuthException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        UUID userUid = UUID.fromString(userId);
        Optional<User> user = userRepository.findById(userUid);
        if (!user.isPresent()) {
            log.warn("Authentication failed: no user found");
            throw new AuthException("Authentication failed: no user found", HttpStatus.UNAUTHORIZED);
        }
        log.info("Authentication success: user {} logged in", user.get().getId());
        return new UserDto((user.get().getId()).toString(), user.get().getRole(), user.get().getLogin());
    }

    public boolean hasPermission(UserDto user, List<UserRole> acceptedRoles) {
        boolean hasPermission = false;
        for (UserRole role: acceptedRoles) {
            if (user.getRole() == role) {
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }

    public UserDto authoriseAndCheckPermission(HttpSession session, List<UserRole> acceptedRoles) throws AuthException {
        UserDto user = authoriseUser(session);
        boolean hasPermission = hasPermission(user, acceptedRoles);
        if (hasPermission) {
            return user;
        }
        String msg = (String.format("Access to denied resource by user %s with role %s", user.getId(), user.getRole()));
        log.warn(msg);
        throw new AuthException(msg, HttpStatus.FORBIDDEN);
    }
}
