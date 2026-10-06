package model.course;

import java.util.ArrayList;
import java.util.List;

public abstract class Course {
    private CourseTopic courseTopic;
    private String title;
    private List<Level> levels;

    public Course(CourseTopic courseTopic, String title, String description) {
        this.courseTopic = courseTopic;
        this.title = title;
        this.levels = new ArrayList<>();
    }
}
