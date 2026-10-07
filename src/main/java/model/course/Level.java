package model.course;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private List<Lesson> lessons;
    private Exam levelExam;

    public Level() {
        this.lessons = new ArrayList<>();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLesson() {
        lessons.add(new Lesson());
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    public void setExam(Exam levelExam) {
        this.levelExam = levelExam;
    }


}
