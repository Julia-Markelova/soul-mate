package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.Message;
import ifmo.soulmate.demo.entities.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IMessageRepository extends JpaRepository<Message, UUID> {

    public List<Message> getMessagesByStatusAndRelativeId(MessageStatus status, UUID relativeId);
}
