package model.course;

import java.util.ArrayList;
import java.util.List;

public class DifficultyLevel {
    private final Difficulty difficulty;
    private List<Unit> units;

    public DifficultyLevel(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.units = new ArrayList<>();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Unit> getUnits() {
        return List.copyOf(units);
    }

    public void addUnit(Unit unit) {
        // TODO: see if we had to check if the unit difficulty is the same as this difficulty
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }
}
