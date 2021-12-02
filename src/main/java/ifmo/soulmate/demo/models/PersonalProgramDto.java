package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.PersonalProgramStatus;

import java.util.List;

public class PersonalProgramDto {

    private String id;
    private String soulId;
    private PersonalProgramStatus status;
    private List<ExerciseDto> exercises;

    public PersonalProgramDto() {
    }

    public PersonalProgramDto(String id, String soulId, PersonalProgramStatus status, List<ExerciseDto> exercises) {
        this.id = id;
        this.soulId = soulId;
        this.status = status;
        this.exercises = exercises;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoulId() {
        return soulId;
    }

    public void setSoulId(String soulId) {
        this.soulId = soulId;
    }

    public PersonalProgramStatus getStatus() {
        return status;
    }

    public void setStatus(PersonalProgramStatus status) {
        this.status = status;
    }

    public List<ExerciseDto> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDto> exercises) {
        this.exercises = exercises;
    }
}
