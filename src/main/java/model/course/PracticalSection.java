package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.exercise.Exercise;
import model.exercise.ExerciseType;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PracticalSection extends Section {
    protected final List<Exercise> exercises;

    @JsonCreator
    public PracticalSection(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("exercises") List<Exercise> exercises) {
        super(title, description, FeatureType.PRACTICAL_SECTION);
        this.exercises = List.copyOf(exercises);
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    Map<ExerciseType, Integer> getExerciseCounts() {
        Map<ExerciseType, Integer> exerciseCounts = new HashMap<>();
        for (Exercise exercise : exercises) {
            exerciseCounts.putIfAbsent(exercise.getType(), 0);
            exerciseCounts.put(exercise.getType(), exerciseCounts.get(exercise.getType()) + 1);
        }
        return exerciseCounts;
    }

    protected List<String> getCurrentStateLogWithCustomName(String name) {
        List<String> log = new ArrayList<>(List.of(new String[]{name + ": { title: " + getTitle() + ", exercises: ["}));

        Map<ExerciseType, Integer> exerciseCounts = getExerciseCounts();

        for (ExerciseType exerciseType : exerciseCounts.keySet()) {
            log.add("\t\t" + exerciseType.toString() + ": " + exerciseCounts.get(exerciseType));
        }

        log.add("\t]");
        log.add("}");
        return log;
    }

    @Override
    public List<String> getCurrentStateLog() {
        return getCurrentStateLogWithCustomName("PracticalSection");
    }
}

