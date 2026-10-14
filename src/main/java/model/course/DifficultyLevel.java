package model.course;

import java.util.ArrayList;
import java.util.List;


public class DifficultyLevel {
    private final Difficulty difficulty;
    private List<Lesson> lessons;
    private Exam levelExam;


    public DifficultyLevel(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.lessons = new ArrayList<>();
        // this.levelExam = new Exam(); // TODO: complete
    }

    public String getDifficulty() {
        return difficulty.getFeatureName();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLesson() {
        lessons.add(new Lesson(this.difficulty));
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    public void setExam(Exam levelExam) {
        this.levelExam = levelExam;
    }


}
