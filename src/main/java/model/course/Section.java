package model.course;

import model.user.Student;
import model.user.User;

import java.util.Objects;

public abstract class Section implements CompletableByStudent {
    private final String title;
    private final String description;

    protected Section(String title, String description) {
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompletedByStudent(Student student) {
        //By defualt, the sections don't have to necessarily be completed (only the unit's exam), although they could in the future.
        return true;
    }
}