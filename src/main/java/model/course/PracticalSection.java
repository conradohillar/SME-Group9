package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.exercise.Exercise;

import java.util.List;

public class PracticalSection extends Section {
    protected final List<Exercise> exercises;

    @JsonCreator
    public PracticalSection(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("exercises") List<Exercise> exercises) {
        super(title, description);
        this.exercises = List.copyOf(exercises);
    }

    // TODO: esto de activeFeatures hay que cambiarlo a la implementacion actual
    public List<Exercise> getVisibleExercises() {
        return exercises;
//        return exercises.stream()
//                .filter(exercise -> activeFeatures.contains(exercise.getType().getFeatureName()))
//                .toList();
    }
}

