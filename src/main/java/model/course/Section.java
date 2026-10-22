package model.course;

import model.LearningMaterial;
import model.exercise.Exercise;
import java.util.List;
import java.util.Set;

public abstract class Section {
    private final String title;

    protected Section(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<LearningMaterial> getVisibleMaterials(Set<String> activeFeatures) {
        return List.of();
    }

    public List<Exercise> getVisibleExercises(Set<String> activeFeatures) {
        return List.of();
    }
}