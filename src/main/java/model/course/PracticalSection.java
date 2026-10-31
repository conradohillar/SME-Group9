package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.FeatureController;
import model.exercise.Exercise;
import model.exercise.ExerciseType;
import model.feature.Feature;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PracticalSection extends Section {
    protected List<Exercise> exercises;

    FeatureController featureController = FeatureController.getInstance();

    public PracticalSection(String title, String description, List<Exercise> exercises, FeatureType featureType) {
        super(title, description, featureType);
        this.exercises = List.copyOf(exercises);
    }

    @JsonCreator
    public PracticalSection(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("exercises") List<Exercise> exercises) {
        this(title, description, exercises, FeatureType.PRACTICAL_SECTION);
    }

    public List<Exercise> getExercises() {
        if (!featureController.isActive(featureType)) {
            return null;
        }
        // we only return the exercises that are active, if none are active we return null
        List<Exercise> toReturn = exercises.stream().filter(e -> featureController.isActive(e.getFeatureType())).toList();
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn;
    }

    Map<ExerciseType, Integer> getExerciseCounts() {
        Map<ExerciseType, Integer> exerciseCounts = new HashMap<>();
        for (Exercise exercise : getExercises()) {
            exerciseCounts.putIfAbsent(exercise.getType(), 0);
            exerciseCounts.put(exercise.getType(), exerciseCounts.get(exercise.getType()) + 1);
        }
        return exerciseCounts;
    }

    protected List<String> getCurrentStateLogWithCustomName(String name) {
        if (!featureController.isActive(featureType)) {
            return List.of();
        }
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

