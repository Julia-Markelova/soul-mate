package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.PersonalProgramStatus;

import java.util.List;

public class PersonalProgramDto {

    private String id;
    private String soulId;
    private PersonalProgramStatus status;
    private Integer progress_percentage;
    private List<ExerciseDto> exercises;

    public PersonalProgramDto() {
    }

    public PersonalProgramDto(String id, String soulId, PersonalProgramStatus status, Integer progress_percentage, List<ExerciseDto> exercises) {
        this.id = id;
        this.soulId = soulId;
        this.status = status;
        this.progress_percentage = progress_percentage;
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

    public Integer getProgress_percentage() {
        return progress_percentage;
    }

    public void setProgress_percentage(Integer progress_percentage) {
        this.progress_percentage = progress_percentage;
    }

    public List<ExerciseDto> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDto> exercises) {
        this.exercises = exercises;
    }
}
