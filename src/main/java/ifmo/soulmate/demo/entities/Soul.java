package ifmo.soulmate.demo.entities;

import ifmo.soulmate.demo.entities.enums.SoulStatus;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="souls")
public class Soul {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SoulStatus status;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "is_mentor")
    private boolean isMentor;

    public Soul() {
    }

    public Soul(UUID id, SoulStatus status, UUID userId, boolean isMentor) {
        this.id = id;
        this.status = status;
        this.userId = userId;
        this.isMentor = isMentor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SoulStatus getStatus() {
        return status;
    }

    public void setStatus(SoulStatus status) {
        this.status = status;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID user_id) {
        this.userId = user_id;
    }

    public boolean getIsMentor() {
        return isMentor;
    }

    public void setIsMentor(boolean is_mentor) {
        this.isMentor = is_mentor;
    }
}
