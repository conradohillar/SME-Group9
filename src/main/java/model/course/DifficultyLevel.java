package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.feature.Feature;
import model.feature.FeatureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class DifficultyLevel extends Feature {
    private final Difficulty difficulty;
    private final List<Unit> units;

    @JsonCreator
    public DifficultyLevel(@JsonProperty("difficulty") Difficulty difficulty, @JsonProperty("units") List<Unit> units) {
        super(FeatureType.DIFFICULTY);
        this.difficulty = difficulty;
        this.units = Objects.requireNonNull(List.copyOf(units));
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<String> getCurrentStateLog(Predicate<FeatureType> isFeatureActive) {
        List<String> log = new ArrayList<>(List.of(new String[]{"DifficultyLevel: { difficulty: " + difficulty + " }", "\tunits: ["}));
        for (Unit unit : units) {
            List<String> unitLog = unit.getCurrentStateLog(isFeatureActive);
            for (String s : unitLog) {
                log.add("\t"+s);
            }
        }
        log.add("\t]");
        return log;
    }
}
