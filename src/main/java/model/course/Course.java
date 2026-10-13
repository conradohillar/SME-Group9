package model.course;

import model.Feature;

import java.util.ArrayList;
import java.util.List;

public abstract class Course extends Feature {
    private CourseTopic courseTopic;
    private String title;
    private String description;
    private List<DifficultyLevel> levels;

    public Course(CourseTopic courseTopic, String title, String description) {
        this.courseTopic = courseTopic;
        this.title = title;
        this.description = description;
        this.levels = new ArrayList<>();
    }

    public List<DifficultyLevel> getLevels() {
        return levels;
    }

    public void addLevel(DifficultyLevel level) {
        this.levels.add(level);
    }

    public void removeLevel(DifficultyLevel level) {
        this.levels.remove(level);
    }
}
