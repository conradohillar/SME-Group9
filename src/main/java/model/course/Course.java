package model.course;

import model.user.User;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private final Topic topic;
    private final String title;
    private final String description;
    private final List<DifficultyLevel> difficultyLevels;

    public Course(Topic topic, String title, String description) {
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.difficultyLevels = new ArrayList<>();
        difficultyLevels.add(new DifficultyLevel(Difficulty.ELEMENTARY));
        difficultyLevels.add(new DifficultyLevel(Difficulty.BEGINNER));
        difficultyLevels.add(new DifficultyLevel(Difficulty.INTERMEDIATE));
        difficultyLevels.add(new DifficultyLevel(Difficulty.ADVANCED));
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

    public List<Unit> getUnitsByDifficulty(Difficulty difficulty) {
        return difficultyLevels.stream()
                .filter(dl -> dl.getDifficulty() == difficulty)
                .findFirst()
                .map(DifficultyLevel::getUnits)
                .orElse(new ArrayList<>());
    }

    public void addUnit(Unit unit) {
        difficultyLevels.get(unit.getDifficulty().ordinal()).addUnit(unit);
    }

    public void removeUnit(Unit unit) {
        difficultyLevels.get(unit.getDifficulty().ordinal()).removeUnit(unit);
    }

    public boolean canDoUnitByUser(User user, Unit unit) {
        // TODO: check if user is properly enrolled in the course and difficulty (database)
        List<Unit> units = getUnitsByDifficulty(unit.getDifficulty());
        int idx = units.indexOf(unit);

        if (idx > 0) {
            Unit previousUnit = units.get(idx - 1);
            return previousUnit.isCompletedByUser(user);
        }

        // This is the first unit in this difficulty.
        // Check the last unit of the previous difficulty, if one exists.
        // TODO: chequear que las difficulties como features esten habilitadas (difficulty y esta y la anterior)
        Difficulty previousDifficulty = getPreviousDifficulty(unit.getDifficulty());
        if (previousDifficulty == null) {
            return true;
        }
        List<Unit> previousUnits = getUnitsByDifficulty(previousDifficulty);
        Unit lastUnit = previousUnits.getLast();
        return lastUnit.isCompletedByUser(user);
    }

    private Difficulty getPreviousDifficulty(Difficulty difficulty) {
        return switch (difficulty) {
            case ELEMENTARY -> null;
            case BEGINNER -> Difficulty.ELEMENTARY;
            case INTERMEDIATE -> Difficulty.BEGINNER;
            case ADVANCED -> Difficulty.INTERMEDIATE;
        };
    }

}
