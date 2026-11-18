package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.feature.Feature;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Unit extends Feature {
    private final String title;
    private final List<Section> sections;
    private final Exam unitExam;

    @JsonCreator
    public Unit(@JsonProperty("title") String title, @JsonProperty("sections") List<Section> sections, @JsonProperty("unitExam") Exam unitExam) {
        super(FeatureType.UNIT);
        this.title = Objects.requireNonNull(title);
        this.sections = Objects.requireNonNull(List.copyOf(sections));
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

    public List<String> getCurrentStateLog(Predicate<FeatureType> isFeatureActive) {
        List<String> log = new ArrayList<>(List.of(new String[]{"Unit: { title: " + title + ", sections: ["}));

        for (Section section : sections) {
            if (!isFeatureActive.test(section.getFeatureType())) {
                continue;
            }
            List<String> sectionLog = section.getCurrentStateLog(isFeatureActive);
            for (String s : sectionLog) {
                log.add("\t"+s);
            }
        }
        log.add("\t],");
        if (unitExam != null) {
            for (String s : unitExam.getCurrentStateLog(isFeatureActive)) {
                log.add("\t"+s);
            }
        }
        log.add("}");
        return log;
    }
}
