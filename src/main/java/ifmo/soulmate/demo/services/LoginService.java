package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.controllers.LoginController;
import ifmo.soulmate.demo.entities.User;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.repositories.UserRepository;
import ifmo.soulmate.demo.exceptions.AuthException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LogManager.getLogger(LoginController.class);

    public UserDto loginUser(String login, String password) throws AuthException {
        Optional<User> user = userRepository.getUserByLoginAndPassword(login, password);
        if (!user.isPresent()) {
            log.warn("Authentication failed: login = {}, password = {}", login, password);
            throw new AuthException("Wrong username or password");
        }
        log.info("Authentication success: user {} logged in", user.get().getId());
        return new UserDto((user.get().getId()).toString(), user.get().getRole(), user.get().getLogin());
    }
}
