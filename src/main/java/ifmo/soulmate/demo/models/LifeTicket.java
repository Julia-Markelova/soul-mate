package ifmo.soulmate.demo.models;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name="life_tickets")
public class LifeTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name="receive_date")
    private Date receiveDate;

    @Column(name = "life_spark_id")
    private UUID life_spark_id;


    public LifeTicket() {
    }

    public LifeTicket(UUID id, Date receiveDate, UUID life_spark_id, boolean is_auto_issued) {
        this.id = id;
        this.receiveDate = receiveDate;
        this.life_spark_id = life_spark_id;
        this.is_auto_issued = is_auto_issued;
    }
    
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

    public UUID getLife_spark_id() {
        return life_spark_id;
    }

    public void setLife_spark_id(UUID life_spark_id) {
        this.life_spark_id = life_spark_id;
    }

    public boolean isIs_auto_issued() {
        return is_auto_issued;
    }

    public void setIs_auto_issued(boolean is_auto_issued) {
        this.is_auto_issued = is_auto_issued;
    }

    @Column(name = "is_auto_issued")
    private boolean is_auto_issued;
}
