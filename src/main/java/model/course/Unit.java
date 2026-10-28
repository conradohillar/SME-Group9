package model.course;

import model.user.Student;
import java.util.List;
import java.util.Objects;

public class Unit implements CompletableByStudent {
    private final String title;
    private final List<Section> sections;
    private final Exam unitExam;

    public Unit(String title, List<Section> sections, Exam unitExam) {
        this.title = Objects.requireNonNull(title);
        this.sections = List.copyOf(sections);
        this.unitExam = unitExam;
    }

    public String getTitle() {
        return title;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Exam getUnitExam() {
        return unitExam;
    }

    public boolean canStudentTakeExam(Student student) {
        return sections.stream().allMatch(section -> section.isCompletedByStudent(student));
    }

    public boolean isCompletedByStudent(Student student) {
        return unitExam.isCompletedByStudent(student);
    }
}
