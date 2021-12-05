package ifmo.soulmate.demo.models;

import java.util.Date;

public class LifeDto {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    String soulId;
    String name;
    String surname;
    int karma;
    Date dateOfBirth;
    Date dateOfDeath;

    public LifeDto() {
    }

    public LifeDto(String id, String soulId, String name, String surname, int karma, Date dateOfBirth, Date dateOfDeath) {
        this.id = id;
        this.soulId = soulId;
        this.name = name;
        this.surname = surname;
        this.karma = karma;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
    }

    public String getSoulId() {
        return soulId;
    }

    public void setSoulId(String soulId) {
        this.soulId = soulId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
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
}
