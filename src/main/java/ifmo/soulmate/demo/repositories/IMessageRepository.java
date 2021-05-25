package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IMessageRepository extends JpaRepository<Message, UUID> {
}
