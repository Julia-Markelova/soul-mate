package ifmo.soulmate.demo.entities;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name="soul_relatives")
public class SoulRelative {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "soul_id")
    private UUID soulId;

    @Column(name = "relative_id")
    private UUID relativeId;

    @Column(name = "notify_relative_about_soul")
    private boolean notifyRelativeAboutSoul;

    public SoulRelative() {
    }

    public SoulRelative(UUID id, UUID soulId, UUID relativeId, boolean notifyRelativeAboutSoul) {
        this.id = id;
        this.soulId = soulId;
        this.relativeId = relativeId;
        this.notifyRelativeAboutSoul = notifyRelativeAboutSoul;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSoulId() {
        return soulId;
    }

    public void setSoulId(UUID soulId) {
        this.soulId = soulId;
    }

    public UUID getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(UUID relativeId) {
        this.relativeId = relativeId;
    }

    public boolean getNotifyRelativeAboutSoul() {
        return notifyRelativeAboutSoul;
    }

    public void setNotifyRelativeAboutSoul(boolean notifyRelativeAboutSoul) {
        this.notifyRelativeAboutSoul = notifyRelativeAboutSoul;
    }
}


