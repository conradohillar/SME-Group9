package model.user;

import model.course.Course;
import model.course.Difficulty;
import model.course.Unit;

import java.util.HashSet;
import java.util.Set;

public class Enrollment {
    private final Course course;
    private Difficulty difficulty;
    private int currentUnitIndex;
    private final Set<Unit> completedUnits;

    public Enrollment(Course course, Difficulty difficulty) {
        this.course = course;
        this.difficulty = difficulty;
        this.currentUnitIndex = 0;
        this.completedUnits = new HashSet<>();
    }

    public Course getCourse() {
        return course;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void advanceToNextDifficulty() {
        if (currentUnitIndex < course.getUnitsByDifficulty(difficulty).size()) {
            throw new IllegalStateException("Cannot advance to next difficulty. Not all units are completed.");
        }
        difficulty = difficulty.getNextDifficulty();
        currentUnitIndex++;
        completedUnits.clear();
    }

    public void advanceToNextUnit() {

    }

}
