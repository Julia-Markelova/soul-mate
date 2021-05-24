package ifmo.soulmate.demo.models;

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

}
