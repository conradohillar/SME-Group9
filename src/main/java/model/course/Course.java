package model.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    private final Topic topic;
    private final String title;
    private final String description;
    private Map<Difficulty, List<Unit>> unitsByDifficulty;

    public Course(Topic topic, String title, String description) {
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.unitsByDifficulty = new HashMap<>();
        unitsByDifficulty.put(Difficulty.ELEMENTARY, new ArrayList<>());
        unitsByDifficulty.put(Difficulty.BEGINNER, new ArrayList<>());
        unitsByDifficulty.put(Difficulty.INTERMEDIATE, new ArrayList<>());
        unitsByDifficulty.put(Difficulty.ADVANCED, new ArrayList<>());
    }

    public Topic getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Map<Difficulty, List<Unit>> getCourseMap() {
        return Map.copyOf(unitsByDifficulty);
    }

    public List<Unit> getUnitsByDifficulty(Difficulty difficulty) {
        return List.copyOf(unitsByDifficulty.get(difficulty));
    }

    public void addUnit(Unit unit) {
        this.unitsByDifficulty.get(unit.getDifficulty()).add(unit);
    }

    public void removeUnit(Unit unit) {
        this.unitsByDifficulty.get(unit.getDifficulty()).remove(unit);
    }
}
