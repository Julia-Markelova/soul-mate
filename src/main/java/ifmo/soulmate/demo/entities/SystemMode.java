package ifmo.soulmate.demo.entities;

import ifmo.soulmate.demo.entities.enums.SystemModeType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="system_modes")
public class SystemMode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "admin_id")
    private UUID adminId;

    @Column(name = "is_manual_mode")
    private Boolean isManualMode;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SystemModeType type;

    public SystemMode() {
    }

    public SystemMode(UUID id, UUID adminId, Boolean isManualMode, SystemModeType type) {
        this.id = id;
        this.adminId = adminId;
        this.isManualMode = isManualMode;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public Boolean getIsManualMode() {
        return isManualMode;
    }

    public void setIsManualMode(Boolean manualMode) {
        isManualMode = manualMode;
    }

    public SystemModeType getType() {
        return type;
    }

    public void setType(SystemModeType type) {
        this.type = type;
    }
}
