package model.course;

import model.exercise.Exercise;

import java.util.List;
import java.util.Set;

public class PracticalSection extends Section {
    private final List<Exercise> exercises;

    public PracticalSection(String title, String description, List<Exercise> exercises) {
        super(title, description);
        this.exercises = List.copyOf(exercises);
    }

    // TODO: esto de activeFeatures hay que cambiarlo a la implementacion actual
    public List<Exercise> getVisibleExercises(Set<String> activeFeatures) {
        return exercises.stream()
                .filter(exercise -> activeFeatures.contains(exercise.getType().getFeatureName()))
                .toList();
    }
}

