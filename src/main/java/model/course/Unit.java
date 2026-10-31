package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.FeatureController;
import model.feature.Feature;
import model.feature.FeatureType;
import model.user.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Unit extends Feature implements CompletableByStudent {
    private final String title;
    private final List<Section> sections;
    private final Exam unitExam;

    private final FeatureController featureController = FeatureController.getInstance();

    @JsonCreator
    public Unit(@JsonProperty("title") String title, @JsonProperty("sections") List<Section> sections, @JsonProperty("unitExam") Exam unitExam) {
        super(FeatureType.UNIT);
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
        List<Section> toReturn = sections.stream().filter(s -> featureController.isActive(s.getFeatureType())).toList();
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn;
    }

    public Exam getUnitExam() {
        if (!FeatureController.getInstance().isActive(FeatureType.EXAM) || unitExam.getExercises() == null) {
            return null;
        }
        return unitExam;
    }

    public boolean canStudentTakeExam(Student student) {
        return sections.stream().allMatch(section -> section.isCompletedByStudent(student));
    }

    public boolean isCompletedByStudent(Student student) {
        return unitExam.isCompletedByStudent(student);
    }

    @Override
    public List<String> getCurrentStateLog() {
        List<String> log = new ArrayList<>(List.of(new String[]{"Unit: { title: " + title + ", sections: ["}));

        for (Section section : getSections()) {
            List<String> sectionLog = section.getCurrentStateLog();
            for (String s : sectionLog) {
                log.add("\t"+s);
            }
        }
        log.add("\t],");
        for (String s : unitExam.getCurrentStateLog()) {
            log.add("\t"+s);
        }
        log.add("}");
        return log;
    }
}
