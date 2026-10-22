package model.course;

import model.user.User;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private final String title;
    private final Difficulty difficulty;
    private List<Section> sections;
    private final Exam unitExam;

    public Unit(String title, Difficulty difficulty, Exam unitExam) {
        this.title = title;
        this.difficulty = difficulty;
        this.sections = new ArrayList<>();
        this.unitExam = unitExam;
    }

    public String getTitle() {
        return title;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public Exam getUnitExam() {
        return unitExam;
    }

    public boolean isCompletedByUser(User user) {
        return unitExam.isPassedByUser(user);
    }

}
