package ifmo.soulmate.demo.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "program_exercises")
public class ProgramExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "program_id")
    private UUID programId;

    @Column(name = "exercise_id")
    private UUID exerciseId;

    @Column(name = "progress")
    private Integer progress;

    public ProgramExercise() {
    }

    public ProgramExercise(UUID id, UUID programId, UUID exerciseId, Integer progress) {
        this.id = id;
        this.programId = programId;
        this.exerciseId = exerciseId;
        this.progress = progress;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProgramId() {
        return programId;
    }

    public void setProgramId(UUID programId) {
        this.programId = programId;
    }

    public UUID getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(UUID exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}