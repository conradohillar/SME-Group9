package model.course;

import model.Feature;

import java.util.ArrayList;
import java.util.List;

public abstract class Course {
    private CourseType courseType;
    private String title;
    private String description;
    private List<DifficultyLevel> levels; // must be ordered (elementary, begginer, intermediate, advanced)

    public Course(CourseType courseType, String title, String description) {
        this.courseType = courseType;
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
