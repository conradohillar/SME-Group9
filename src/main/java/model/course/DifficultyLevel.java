package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.feature.Feature;
import model.feature.FeatureType;
import model.user.Student;

import java.util.ArrayList;
import java.util.List;

public class DifficultyLevel extends Feature implements CompletableByStudent {
    private final Difficulty difficulty;
    List<Unit> units;

    @JsonCreator
    public DifficultyLevel(@JsonProperty("difficulty") Difficulty difficulty, @JsonProperty("units") List<Unit> units) {
        super(FeatureType.DIFFICULTY);
        this.difficulty = difficulty;
        this.units = List.copyOf(units);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public boolean isCompletedByStudent(Student student) {
        return units.stream().allMatch(u -> u.isCompletedByStudent(student));
    }

    @Override
    public List<String> getCurrentStateLog() {
        List<String> log = new ArrayList<>(List.of(new String[]{"DifficultyLevel: { difficulty: " + difficulty + " }", "\tunits: ["}));
        for (Unit unit : units) {
            List<String> unitLog = unit.getCurrentStateLog();
            for (String s : unitLog) {
                log.add("\t"+s);
            }
        }
        log.add("\t]");
        return log;
    }
}
