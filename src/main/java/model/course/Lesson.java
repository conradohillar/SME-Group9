package model.course;

import model.LearningMaterial;
import model.exercise.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lesson {
    private Difficulty difficulty;
    private List<Section> sections;

    public Lesson(Difficulty difficulty) {
        this.sections = new ArrayList<>();
        this.difficulty = difficulty;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection(Section section) {
        this.sections.remove(section);
    }

    public List<LearningMaterial> visibleMaterials(Set<String> activeFeatures) {
        return sections.stream().flatMap(section ->
                section.visibleMaterials(activeFeatures).stream()).toList();
    }

    public List<Exercise> visibleExercises(Set<String> activeFeatures) {
        return sections.stream().flatMap(section ->
                    section.visibleExercises(activeFeatures).stream()).toList();
    }
}
