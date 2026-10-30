package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.exam.Exam;
import model.user.Student;
import java.util.List;
import java.util.Objects;

public class Unit implements CompletableByStudent {
    private final String title;
    private final List<Section> sections;
    private final Exam unitExam;

    @JsonCreator
    public Unit(@JsonProperty("title") String title, @JsonProperty("sections") List<Section> sections, @JsonProperty("unitExam") Exam unitExam) {
        this.title = Objects.requireNonNull(title);
        this.sections = List.copyOf(sections);
        this.unitExam = unitExam;
        if (this.unitExam != null) {
            unitExam.setUnit(this);
        }
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
