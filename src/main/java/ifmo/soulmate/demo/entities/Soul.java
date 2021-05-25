package ifmo.soulmate.demo.entities;

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
    private UUID user_id;

    @Column(name = "is_mentor")
    private boolean is_mentor;

    public Soul() {
    }

    public Soul(UUID id, SoulStatus status, UUID user_id, boolean is_mentor) {
        this.id = id;
        this.status = status;
        this.user_id = user_id;
        this.is_mentor = is_mentor;
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

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public boolean getIs_mentor() {
        return is_mentor;
    }

    public void setIs_mentor(boolean is_mentor) {
        this.is_mentor = is_mentor;
    }
}
