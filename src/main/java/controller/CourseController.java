package controller;

import database.Database;
import model.course.Course;
import model.course.Difficulty;
import model.course.DifficultyLevel;
import model.course.Unit;
import model.feature.FeatureType;
import model.user.Student;

public class CourseController {
    private static CourseController instance;
    private static final int COINS_PER_UNIT = 2;

    private static final FeatureController featureController = FeatureController.getInstance();

    private CourseController() {
    }

    public static CourseController getInstance() {
        if (instance == null) {
            instance = new CourseController();
        }
        return instance;
    }

    public boolean canShowCourse(Course course) {
        Student currentStudent = getCurrentStudent();
        return Database.getInstance().isStudentEnrolled(currentStudent, course);
    }

    public boolean isCoursePassed(Course course) {
        Student currentStudent = getCurrentStudent();
        if (!Database.getInstance().isStudentEnrolled(currentStudent, course)) {
            return false;
        }
        Database database = Database.getInstance();

        return isDifficultyLevelPassed(database.getEnrollment(currentStudent, course).getCourse().getDifficultyLevels().getLast());
    }

    public boolean isDifficultyLevelUnlocked(Course course, DifficultyLevel difficultyLevel) {
        Student currentStudent = getCurrentStudent();
        Database database = Database.getInstance();
        Difficulty currentDifficulty = database.getCurrentDifficulty(currentStudent, course);
        Difficulty levelDifficulty = difficultyLevel.getDifficulty();

        return levelDifficulty.ordinal() <= currentDifficulty.ordinal();
    }

    public boolean isDifficultyLevelPassed(DifficultyLevel level) {
        Student currentStudent = getCurrentStudent();

        return level.getUnits().getLast().getUnitExam().isCompletedByStudent(currentStudent);
    }

    public boolean isUnitUnlocked(Course course, Unit unit) {
        Student currentStudent = getCurrentStudent();
        Database database = Database.getInstance();

        int currentUnitIndex = database.getCurrentUnitIndex(currentStudent, course);
        if (currentUnitIndex < 0) {
            return false;
        }

        Difficulty currentDifficulty = database.getCurrentDifficulty(currentStudent, course);
        Difficulty unitDifficulty = course.getDifficultyOfUnit(unit);

        if (unitDifficulty.ordinal() < currentDifficulty.ordinal()) {
            return true;
        }
        if (unitDifficulty.ordinal() > currentDifficulty.ordinal()) {
            return false;
        }

        boolean unlockedByProgress = course.getUnitsByDifficulty(currentDifficulty).indexOf(unit) <= currentUnitIndex;
        boolean unlockedBySubscriptionLimit = currentStudent.getRemainingUnits() > 0 || currentStudent.getRemainingUnits() == -1;

        return unlockedByProgress && unlockedBySubscriptionLimit;
    }

    public boolean isUnitPassed(Unit unit) {
        Student currentStudent = getCurrentStudent();
        return unit.getUnitExam().isCompletedByStudent(currentStudent);
    }

    public void passUnitForStudent(Student student, Unit unit, Course course, Difficulty difficulty) {
        Database database = Database.getInstance();
        Database.EnrollmentDto enrollment = database.getEnrollment(student, course);

        if (enrollment.getDifficulty() != difficulty) {
            throw new IllegalStateException("The student is not enrolled in this difficulty.");
        }
        if (!unit.canStudentTakeExam(student)) {
            throw new IllegalStateException("The student cannot skip units.");
        }

        student.addCoins(COINS_PER_UNIT);

        if (unit.equals(course.getUnitsByDifficulty(difficulty).getLast())) {
            Difficulty nextDifficulty = difficulty.getNextDifficulty();
            if (nextDifficulty == null) {
                student.progressUnit();
                return;  // the course has been completed
            }
            enrollment.setDifficulty(nextDifficulty);
            enrollment.setCurrentUnitIndex(0);
        } else {
            enrollment.setCurrentUnitIndex(enrollment.getCurrentUnitIndex() + 1);
        }
        student.progressUnit();
    }

    // active or inactive features ------------------------------------------------

    public boolean shouldShowTheoreticalSection() {
        return featureController.isActive(FeatureType.THEORETICAL_SECTION);
    }

    public boolean shouldShowPracticalSection() {
        return featureController.isActive(FeatureType.PRACTICAL_SECTION);
    }

    public boolean shouldShowExam() {
        return featureController.isActive(FeatureType.EXAM);
    }

    private Student getCurrentStudent() {
        return AuthController.getInstance().getLoggedStudent();
    }
}
