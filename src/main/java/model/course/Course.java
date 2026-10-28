package model.course;

import model.user.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements CompletableByStudent {
    private final Topic topic;
    private final String title;
    private final String description;
    private final List<DifficultyLevel> difficultyLevels;

    public Course(Topic topic, String title, String description, List<DifficultyLevel> difficultyLevels) {
        this.topic = Objects.requireNonNull(topic);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.difficultyLevels = List.copyOf(difficultyLevels);
    }

    public Topic getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<DifficultyLevel> getDifficultyLevels() {
        return difficultyLevels;
    }

    public List<Unit> getUnitsByDifficulty(Difficulty difficulty) {
        return difficultyLevels.stream()
                .filter(dl -> dl.getDifficulty() == difficulty)
                .findFirst()
                .map(DifficultyLevel::getUnits)
                .orElse(List.of());
    }

    public boolean isCompletedByStudent(Student student) {
        return difficultyLevels.stream().allMatch(dl -> dl.isCompletedByStudent(student));
    }

    public boolean isStudentEnrolled(Student student) {
        // TODO: completar
        return true;
    }
}
