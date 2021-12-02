package ifmo.soulmate.demo.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="life_sparks")
public class LifeSpark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name="receive_date")
    private Date receiveDate;

    @Column(name = "received_by")
    private UUID received_by;

    @Column(name = "issued_by")
    private UUID issued_by;

    @Column(name = "personal_program_id")
    private UUID personal_program_id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public UUID getReceived_by() {
        return received_by;
    }

    public void setReceived_by(UUID received_by) {
        this.received_by = received_by;
    }

    public UUID getIssued_by() {
        return issued_by;
    }

    public void setIssued_by(UUID issued_by) {
        this.issued_by = issued_by;
    }

    public UUID getPersonal_program_id() {
        return personal_program_id;
    }

    public void setPersonal_program_id(UUID personal_program_id) {
        this.personal_program_id = personal_program_id;
    }

    public LifeSpark() {
    }

    public LifeSpark(UUID id, Date receiveDate, UUID received_by, UUID personal_program_id) {
        this.id = id;
        this.receiveDate = receiveDate;
        this.received_by = received_by;
        this.personal_program_id = personal_program_id;
    }

    public LifeSpark(UUID id, Date receiveDate, UUID received_by, UUID issued_by, UUID personal_program_id) {
        this.id = id;
        this.receiveDate = receiveDate;
        this.received_by = received_by;
        this.issued_by = issued_by;
        this.personal_program_id = personal_program_id;
    }
}
