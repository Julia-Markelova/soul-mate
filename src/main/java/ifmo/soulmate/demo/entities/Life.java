package ifmo.soulmate.demo.entities;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="lifes")
public class Life {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "soul_id")
    private UUID soulId;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "date_of_death")
    private Date dateOfDeath;

    @Column(name = "soul_name")
    private String soulName;

    @Column(name = "soul_surname")
    private String soulSurname;

    @Column(name = "karma")
    private int karma;

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getSoulName() {
        return soulName;
    }

    public void setSoulName(String soulName) {
        this.soulName = soulName;
    }

    public String getSoulSurname() {
        return soulSurname;
    }

    public void setSoulSurname(String soulSurname) {
        this.soulSurname = soulSurname;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public Life() {
    }

    public Life(UUID id, UUID soulId, Date dateOfBirth, Date dateOfDeath, String soulName, String soulSurname, int karma) {
        this.id = id;
        this.soulId = soulId;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.soulName = soulName;
        this.soulSurname = soulSurname;
        this.karma = karma;
    }

}
