package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.Exercise;
import ifmo.soulmate.demo.entities.LifeSpark;
import ifmo.soulmate.demo.entities.PersonalProgram;
import ifmo.soulmate.demo.entities.ProgramExercise;
import ifmo.soulmate.demo.entities.enums.PersonalProgramStatus;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.ExerciseDto;
import ifmo.soulmate.demo.models.PersonalProgramDto;
import ifmo.soulmate.demo.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonalProgramService {

    @Autowired
    SoulRepository soulRepository;
    @Autowired
    PersonalProgramRepository personalProgramRepository;
    @Autowired
    ProgramExerciseRepository programExerciseRepository;
    @Autowired
    ExerciseRepository exerciseRepository;
    @Autowired
    SystemModeRepository systemModeRepository;
    @Autowired
    ILifeSparkRepository lifeSparkRepository;

    private static final Logger log = LogManager.getLogger(PersonalProgramService.class);

    public PersonalProgramDto getPersonalProgramBySoulId(UUID soulId) throws NonExistingEntityException {
        Optional<PersonalProgram> personalProgram = personalProgramRepository.findPersonalProgramBySoulId(soulId);
        if (!personalProgram.isPresent()) {
            String msg = String.format("No program found for soul %s", soulId.toString());
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        log.info("Get personal program for soul {}", soulId);
        PersonalProgram program = personalProgram.get();
        List<ProgramExercise> programExercises = programExerciseRepository.findProgramExerciseByProgramId(program.getId());
        List<ExerciseDto> exerciseDtos =
                programExercises
                        .stream()
                        .map(x -> {
                            try {
                                Optional<Exercise> exercise = exerciseRepository.findById(x.getExerciseId());
                                if (!exercise.isPresent()) {
                                    throw new NonExistingEntityException("No such exercise found");
                                }
                                return new ExerciseDto(exercise.get().getId().toString(), exercise.get().getName(),
                                        exercise.get().getSkill(), x.getProgress());
                            } catch (Exception e) {
                                log.warn(e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        return new PersonalProgramDto(
                program.getId().toString(),
                program.getSoulId().toString(),
                program.getStatus(),
                exerciseDtos
        );
    }

    public void updateProgramExerciseProgress(UUID programId, UUID exerciseId, Integer progress) throws NonExistingEntityException {
        /*
          Обновляет прогресс у упражнения, если персональня программа еще не завершена.
          Если после обновления прогресса все упраженения в программе выполнены,
          то статус програмы становится завершенным.
         */
        if (isAllExercisesInProgramFinished(programId)) {
            String msg = String.format("Can not change program %s: it was already finished", programId.toString());
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }

        Optional<ProgramExercise> exercise = programExerciseRepository.findProgramExerciseByProgramIdAndExerciseId(
                programId, exerciseId);
        if (!exercise.isPresent()) {
            String msg = (String.format("No exercise found with id: %s", exerciseId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        ProgramExercise unwrapped = exercise.get();
        if (progress < 0 || progress > 100) {
            String msg = (String.format("Incorrect value for update: %d. Progress must be in [0;100]. ", progress));
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }
        unwrapped.setProgress(progress);
        programExerciseRepository.saveAndFlush(unwrapped);

        if (isAllExercisesInProgramFinished(programId)) {
            updatePersonalProgramStatus(programId, PersonalProgramStatus.SUCCESS);
            log.info(String.format("Program %s was successfully finished", programId.toString()));
        } else {
            updatePersonalProgramStatus(programId, PersonalProgramStatus.IN_PROGRESS);
            log.info(String.format("Program %s in progress", programId.toString()));
        }
    }

    public PersonalProgramDto createPersonalProgram(UUID soulId, Integer numberOfExercises) {
        /*
        Создает новую персональную программу, состоящую из :numberOfExercises: упражнений.
        */
        PersonalProgram personalProgram = new PersonalProgram(UUID.randomUUID(), soulId,
                new Date(), 0, PersonalProgramStatus.NEW);
        personalProgramRepository.saveAndFlush(personalProgram);
        List<Exercise> exercises = exerciseRepository.findAll();
        if (numberOfExercises > exercises.size()) {
            throw new IllegalArgumentException(String.format("Too much exercises requested. Max value %d", exercises.size()));
        }

        // достаем N рандомных упражнений для составления программы
        Collections.shuffle(exercises);
        List<Exercise> programExercises = exercises.stream().limit(numberOfExercises).collect(Collectors.toList());
        List<ExerciseDto> exerciseDtos =
                programExercises
                        .stream()
                        .map(x -> {
                            try {
                                ProgramExercise programExercise = new ProgramExercise(UUID.randomUUID(), personalProgram.getId(), x.getId(), 0);
                                programExerciseRepository.saveAndFlush(programExercise);
                                return new ExerciseDto(x.getId().toString(), x.getName(), x.getSkill(), programExercise.getProgress());
                            } catch (Exception e) {
                                log.warn(e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        return new PersonalProgramDto(personalProgram.getId().toString(), personalProgram.getSoulId().toString(), personalProgram.getStatus(), exerciseDtos);
    }

    private void updatePersonalProgramStatus(UUID programId, PersonalProgramStatus status) throws NonExistingEntityException {
        /*
        Обновляет статус у персональной программы. Если программа завершена успешно,
        и установлен автоматический режим выдачи искр жизни, то создаем новую искру.
        * */
        Optional<PersonalProgram> personalProgram = personalProgramRepository.findById(programId);
        if (!personalProgram.isPresent()) {
            String msg = (String.format("No exercise found with id: %s", programId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        PersonalProgram unwrapped = personalProgram.get();
        if (unwrapped.getStatus() != status) {
            unwrapped.setStatus(status);
            personalProgramRepository.saveAndFlush(unwrapped);

            if (status == PersonalProgramStatus.SUCCESS) {
                // создаем новую искру жизни при успешном прохождении программы
                LifeSpark lifeSpark = new LifeSpark(UUID.randomUUID(), new Date(), personalProgram.get().getSoulId(), personalProgram.get().getSoulId(), personalProgram.get().getId());
                lifeSparkRepository.saveAndFlush(lifeSpark);

            }
        }
    }


    private boolean isAllExercisesInProgramFinished(UUID programId) throws NonExistingEntityException {
        Optional<PersonalProgram> personalProgram = personalProgramRepository.findById(programId);
        if (!personalProgram.isPresent()) {
            String msg = (String.format("No exercise found with id: %s", programId.toString()));
            log.warn(msg);
            throw new NonExistingEntityException(msg);
        }
        List<ProgramExercise> programExercises = programExerciseRepository.findProgramExerciseByProgramId(programId);
        for (ProgramExercise p : programExercises) {
            if (p.getProgress() < 100) {
                return false;
            }
        }
        return true;
    }
}
