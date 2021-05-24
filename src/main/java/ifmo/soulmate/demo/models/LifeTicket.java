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

    @Column(name = "is_auto_issued")
    private boolean is_auto_issued;
}
