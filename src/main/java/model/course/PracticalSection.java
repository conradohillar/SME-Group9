package model.course;

import model.exercise.Exercise;

import java.util.List;
import java.util.Set;

public class PracticalSection extends Section {
    private final List<Exercise> exercises;

    public PracticalSection(String title, List<Exercise> exercises) {
        super(title);
        this.exercises = List.copyOf(exercises);
    }

    @Override
    public List<Exercise> getVisibleExercises(Set<String> activeFeatures) {
        return exercises.stream()
                .filter(exercise -> activeFeatures.contains(exercise.getType().getFeatureName()))
                .toList();
    }
}

