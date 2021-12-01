package ifmo.soulmate.demo.models;

public class ExerciseDto {

    private String id;
    private String name;
    private String skill;
    private Integer progress;

    public ExerciseDto() {
    }

    public ExerciseDto(String id, String name, String skill, Integer progress) {
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
