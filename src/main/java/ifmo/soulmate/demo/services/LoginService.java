package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.controllers.LoginController;
import ifmo.soulmate.demo.entities.God;
import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.entities.User;
import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.UserDto;
import ifmo.soulmate.demo.repositories.GodRepository;
import ifmo.soulmate.demo.repositories.SoulRepository;
import ifmo.soulmate.demo.repositories.UserRepository;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GodRepository godRepository;
    @Autowired
    private SoulRepository soulRepository;
    private static final Logger log = LogManager.getLogger(LoginController.class);

    private final String jwtSecret = "top-secret-string";

    public String generateToken(String userId) {
        Date date = Date.from(LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.warn("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.warn("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.warn("Malformed jwt");
        } catch (SignatureException sEx) {
            log.warn("Invalid signature");
        } catch (Exception e) {
            log.warn("invalid token");
        }
        return false;
    }

    public String getUserIdFromToken(String token) throws AuthException {
        String msg;
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException expEx) {
            msg = "Token expired";
        } catch (UnsupportedJwtException unsEx) {
            msg = "Unsupported jwt";
        } catch (MalformedJwtException mjEx) {
            msg = "Malformed jwt";
        } catch (SignatureException sEx) {
            msg = "Invalid signature";
        } catch (Exception e) {
            msg = "invalid token";
        }
        log.warn(msg);
        throw new AuthException(msg, HttpStatus.UNAUTHORIZED);
    }

    public UserDto loginUser(String login, String password) throws AuthException, NonExistingEntityException {
        Optional<User> user = userRepository.getUserByLoginAndPassword(login, password);
        if (!user.isPresent()) {
            log.warn("Authentication failed: login = {}, password = {}", login, password);
            throw new AuthException("Wrong username or password", HttpStatus.UNAUTHORIZED);
        }
        String token = generateToken(user.get().getId().toString());
        log.info("Authentication success: user {} logged in", user.get().getId());
        return mapUserToUserDto(user.get(), token);
    }

    public UserDto authoriseUser(String token) throws AuthException, NonExistingEntityException {
        String userId = getUserIdFromToken(token);
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
        User unwrapped = user.get();
        return mapUserToUserDto(unwrapped, token);
    }

    public boolean hasPermission(UserDto user, List<UserRole> acceptedRoles) {
        boolean hasPermission = false;
        for (UserRole role : acceptedRoles) {
            if (user.getRole() == role) {
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }

    public UserDto authoriseAndCheckPermission(String token, List<UserRole> acceptedRoles) throws AuthException, NonExistingEntityException {
        UserDto user = authoriseUser(token);
        boolean hasPermission = hasPermission(user, acceptedRoles);
        if (hasPermission) {
            return user;
        }
        String msg = (String.format("Access to denied resource by user %s with role %s", user.getId(), user.getRole()));
        log.warn(msg);
        throw new AuthException(msg, HttpStatus.FORBIDDEN);
    }

    private UserDto mapUserToUserDto(User user, String token) throws NonExistingEntityException {
        if (user.getRole() == UserRole.ADMIN) {
            return new UserDto(user.getId().toString(), user.getRole(), user.getLogin(), token);
        }
        if (user.getRole() == UserRole.GOD) {
            Optional<God> god = godRepository.getByUserId(user.getId());
            if (god.isPresent()) {
                UUID roleId = god.get().getId();
                return new UserDto(user.getId().toString(), user.getRole(), user.getLogin(),
                        token, roleId.toString());
            }
            log.warn("No God for userId {}", user.getId());
            throw new NonExistingEntityException("No God for userId");
        }
        Optional<Soul> soul = soulRepository.getByUserId(user.getId());
        if (soul.isPresent()) {
            UUID roleId = soul.get().getId();
            return new UserDto(user.getId().toString(), user.getRole(), user.getLogin(),
                    token, roleId.toString());
        }
        log.warn("No Soul for userId {}", user.getId());
        throw new NonExistingEntityException("No Soul for userId");
    }

}
