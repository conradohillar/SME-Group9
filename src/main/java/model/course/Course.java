package model.course;

import java.util.ArrayList;
import java.util.List;

public abstract class Course {
    private CourseTopic courseTopic;
    private String title;
    private String description;
    private List<Level> levels;

    public Course(CourseTopic courseTopic, String title, String description) {
        this.courseTopic = courseTopic;
        this.title = title;
        this.description = description;
        this.levels = new ArrayList<>();
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void addLevel(Level level) {
        this.levels.add(level);
    }

    public void removeLevel(Level level) {
        this.levels.remove(level);
    }
}
