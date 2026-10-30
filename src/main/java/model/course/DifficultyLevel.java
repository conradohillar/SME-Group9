package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.user.Student;
import model.user.User;
import java.util.List;

public class DifficultyLevel implements CompletableByStudent {
    private final Difficulty difficulty;
    private final List<Unit> units;

    @JsonCreator
    public DifficultyLevel(@JsonProperty("difficulty") Difficulty difficulty, @JsonProperty("units") List<Unit> units) {
        this.difficulty = difficulty;
        this.units = units;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Unit> getUnits() {
        return List.copyOf(units);
    }

    public boolean isCompletedByStudent(Student student) {
        return units.stream().allMatch(u -> u.isCompletedByStudent(student));
    }
}
