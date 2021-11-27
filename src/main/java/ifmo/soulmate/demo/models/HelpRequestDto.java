package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.HelpRequestStatus;

public class HelpRequestDto {
    private String id;
    private String createdBy;
    private String acceptedBy;
    private HelpRequestStatus status;

    public HelpRequestDto(String id, String createdBy, HelpRequestStatus status) {
        this.id = id;
        this.createdBy = createdBy;
        this.status = status;
    }

    public HelpRequestDto() {
    }

    public HelpRequestDto(String id, String createdBy, String acceptedBy, HelpRequestStatus status) {
        this.id = id;
        this.createdBy = createdBy;
        this.acceptedBy = acceptedBy;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public HelpRequestStatus getStatus() {
        return status;
    }

    public void setStatus(HelpRequestStatus status) {
        this.status = status;
    }
}

