package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.feature.Feature;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Course extends Feature {
    private final Topic topic;
    private final String title;
    private final String description;
    private final List<DifficultyLevel> difficultyLevels;

    @JsonCreator
    public Course(@JsonProperty("topic") Topic topic, @JsonProperty ("title") String title, @JsonProperty ("description") String description, @JsonProperty ("difficultyLevels") List<DifficultyLevel> difficultyLevels) {
        this.topic = Objects.requireNonNull(topic);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.difficultyLevels = Objects.requireNonNull(List.copyOf(difficultyLevels));
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

    public DifficultyLevel getLastDifficultyLevel() {
        return difficultyLevels.getLast();
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

    public List<String> getCurrentStateLog(Predicate<FeatureType> isFeatureActive) {
        List<String> log = new ArrayList<>(List.of(new String[]{"Course: { topic: " + topic + ", title: " + title + " }", "\tdifficultyLevels: ["}));
        for (DifficultyLevel dl : difficultyLevels) {
            List<String> dlLog = dl.getCurrentStateLog(isFeatureActive);
            for (String s : dlLog) {
                log.add("\t"+s);
            }
        }
        log.add("\t]");
        return log;
    }
}
