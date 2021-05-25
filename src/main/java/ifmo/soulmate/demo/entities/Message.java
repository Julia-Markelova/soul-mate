package ifmo.soulmate.demo.entities;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "relative_id")
    private UUID relativeId;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private MessageStatus status;

    public Message() {
    }

    public Message(UUID id, UUID relativeId, String message, MessageStatus status) {
        this.id = id;
        this.relativeId = relativeId;
        this.message = message;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(UUID relativeId) {
        this.relativeId = relativeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}

