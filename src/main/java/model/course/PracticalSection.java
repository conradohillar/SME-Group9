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
import java.util.function.Predicate;

public class PracticalSection extends Section {
    private final List<Exercise> exercises;

    public PracticalSection(String title, String description, List<Exercise> exercises, FeatureType featureType) {
        super(title, description, featureType);
        this.exercises = List.copyOf(exercises);
    }

    @JsonCreator
    public PracticalSection(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("exercises") List<Exercise> exercises) {
        this(title, description, exercises, FeatureType.PRACTICAL_SECTION);
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    private Map<ExerciseType, Integer> getExerciseCounts(Predicate<FeatureType> isFeatureActive) {
        Map<ExerciseType, Integer> exerciseCounts = new HashMap<>();
        for (Exercise exercise : exercises) {
            if (!isFeatureActive.test(exercise.getFeatureType())) {
                continue;
            }
            exerciseCounts.putIfAbsent(exercise.getType(), 0);
            exerciseCounts.put(exercise.getType(), exerciseCounts.get(exercise.getType()) + 1);
        }
        return exerciseCounts;
    }

    protected List<String> getCurrentStateLogWithCustomName(String name, Predicate<FeatureType> isFeatureActive) {
        if (!isFeatureActive.test(getFeatureType())) {
            return List.of();
        }
        List<String> log = new ArrayList<>(List.of(new String[]{name + ": { title: " + getTitle() + ", exercises: ["}));

        Map<ExerciseType, Integer> exerciseCounts = getExerciseCounts(isFeatureActive);

        for (ExerciseType exerciseType : exerciseCounts.keySet()) {
            log.add("\t\t" + exerciseType.toString() + ": " + exerciseCounts.get(exerciseType));
        }

        log.add("\t]");
        log.add("}");
        return log;
    }

    @Override
    public List<String> getCurrentStateLog(Predicate<FeatureType> isFeatureActive) {
        return getCurrentStateLogWithCustomName("PracticalSection", isFeatureActive);
    }
}

