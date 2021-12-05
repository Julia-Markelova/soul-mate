package ifmo.soulmate.demo.entities;

import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;
import ifmo.soulmate.demo.entities.enums.HelpRequestType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "help_requests")
public class HelpRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private HelpRequestStatus status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private HelpRequestType type;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "accepted_by")
    private UUID acceptedBy;

    public HelpRequest() {
    }

    public HelpRequest(UUID id, HelpRequestStatus status, UUID createdBy, HelpRequestType type) {
        this.id = id;
        this.status = status;
        this.createdBy = createdBy;
        this.type = type;
    }

    public HelpRequest(UUID id, HelpRequestStatus status, UUID createdBy, UUID acceptedBy, HelpRequestType type) {
        this.id = id;
        this.status = status;
        this.createdBy = createdBy;
        this.acceptedBy = acceptedBy;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public HelpRequestStatus getStatus() {
        return status;
    }

    public void setStatus(HelpRequestStatus status) {
        this.status = status;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(UUID acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public HelpRequestType getType() {
        return type;
    }

    public void setType(HelpRequestType type) {
        this.type = type;
    }
}
