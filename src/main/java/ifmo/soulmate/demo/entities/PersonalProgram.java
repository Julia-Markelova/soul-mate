package ifmo.soulmate.demo.entities;


import ifmo.soulmate.demo.entities.enums.PersonalProgramStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "personal_programs")
public class PersonalProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "soul_id")
    private UUID soulId;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "progress_percentage")
    private Integer progressPercenatage;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PersonalProgramStatus status;

    public PersonalProgram() {
    }

    public PersonalProgram(UUID id, UUID soulId, Date createdDate, Integer progressPercenatage, PersonalProgramStatus status) {
        this.id = id;
        this.soulId = soulId;
        this.createdDate = createdDate;
        this.progressPercenatage = progressPercenatage;
        this.status = status;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getProgressPercenatage() {
        return progressPercenatage;
    }

    public void setProgressPercenatage(Integer progressPercenatage) {
        this.progressPercenatage = progressPercenatage;
    }

    public PersonalProgramStatus getStatus() {
        return status;
    }

    public void setStatus(PersonalProgramStatus status) {
        this.status = status;
    }
}
