package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> getUserByLoginAndPassword(String login, String password);
}