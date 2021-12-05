package ifmo.soulmate.demo.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rejected_requests")
public class RejectedRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "rejected_by")
    private UUID rejectedBy;

    @Column(name = "request_id")
    private UUID requestId;

    public RejectedRequest() {
    }

    public RejectedRequest(UUID id, UUID rejectedBy, UUID requestId) {
        this.id = id;
        this.rejectedBy = rejectedBy;
        this.requestId = requestId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(UUID rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

}