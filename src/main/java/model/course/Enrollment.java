package model.course;

public class Enrollment {
    private final Course course;
    private Difficulty difficulty;
    private int currentUnitIndex;

    public Enrollment(Course course) {
        this.course = course;
        this.difficulty = Difficulty.ELEMENTARY;
        this.currentUnitIndex = 0;
    }

    public Course getCourse() {
        return course;
    }

    public boolean isAt(Difficulty difficulty) { return this.difficulty == difficulty; }

    public Difficulty getCurrentDifficulty() {
        return difficulty;
    }
    public int getCurrentUnitIndex() { return currentUnitIndex; }

    public boolean isCurrentUnit(Unit unit) {
        return course.getUnitsByDifficulty(difficulty).indexOf(unit) == currentUnitIndex;
    }

    public void passUnit(Unit unit) {
        if (!unit.equals(course.getUnitsByDifficulty(difficulty).getLast())) {
            currentUnitIndex++;
            return;
        }
        Difficulty next = difficulty.getNextDifficulty();
        if (next != null) {
            difficulty = next;
            currentUnitIndex = 0;
        }
    }
}
