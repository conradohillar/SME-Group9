package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.feature.Feature;
import model.feature.FeatureType;
import model.user.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course extends Feature implements CompletableByStudent {
    private final Topic topic;
    private final String title;
    private final String description;
    private final List<DifficultyLevel> difficultyLevels;

    @JsonCreator
    public Course(@JsonProperty("topic") Topic topic, @JsonProperty ("title") String title, @JsonProperty ("description") String description, @JsonProperty ("difficultyLevels") List<DifficultyLevel> difficultyLevels) {
        this.topic = Objects.requireNonNull(topic);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.difficultyLevels = List.copyOf(difficultyLevels);
        super(FeatureType.COURSE);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<DifficultyLevel> getDifficultyLevels() {
        return difficultyLevels;
    }

    public Difficulty getDifficultyOfUnit(Unit unit) {
        return difficultyLevels.stream()
                .filter(dl -> dl.getUnits().contains(unit))
                .map(DifficultyLevel::getDifficulty)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unit does not belong to this course."
                ));
    }

    public List<Unit> getUnitsByDifficulty(Difficulty difficulty) {
        return difficultyLevels.stream()
                .filter(dl -> dl.getDifficulty() == difficulty)
                .findFirst()
                .map(DifficultyLevel::getUnits)
                .orElse(List.of());
    }

    public boolean isCompletedByStudent(Student student) {
        return difficultyLevels.stream().allMatch(dl -> dl.isCompletedByStudent(student));
    }

    @Override
    public List<String> getCurrentStateLog() {
        List<String> log = new ArrayList<>(List.of(new String[]{"Course: { topic: " + topic + ", title: " + title + " }", "\tdifficultyLevels: ["}));
        for (DifficultyLevel dl : difficultyLevels) {
            List<String> dlLog = dl.getCurrentStateLog();
            for (String s : dlLog) {
                log.add("\t"+s);
            }
        }
        log.add("\t]");
        return log;
    }
}
