package model.course;

import model.user.Student;
import model.user.User;
import java.util.List;

public class DifficultyLevel implements CompletableByStudent {
    private final Difficulty difficulty;
    private final List<Unit> units;

    public DifficultyLevel(Difficulty difficulty, List<Unit> units) {
        this.difficulty = difficulty;
        this.units = units;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Unit> getUnits() {
        return List.copyOf(units);
    }

    public boolean canStudentDoUnit(Student student, Unit unit) {
        // TODO: check if the student is properly enrolled
        student.canDoNextUnit();
        int idx = units.indexOf(unit);

        // if it's the first unit in the level, the student can do it. If not, the
        // previous unit has to have been completed to be able to do this one
        if (idx > 0) {
            Unit previousUnit = units.get(idx - 1);
            return previousUnit.isCompletedByStudent(student);
        }
        return true;
    }

    public boolean isCompletedByStudent(Student student) {
        return units.stream().allMatch(u -> u.isCompletedByStudent(student));
    }
}
