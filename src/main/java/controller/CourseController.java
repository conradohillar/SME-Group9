
package controller;

import database.Database;
import model.course.*;
import model.feature.FeatureType;
import model.subscription.SubscriptionTier;
import model.user.Student;

public class CourseController {

    private static final CourseController instance = new CourseController();
    private static final int COINS_PER_UNIT = 2;

    private static final FeatureController featureController = FeatureController.getInstance();

    private CourseController() {
    }

    public static CourseController getInstance() {
        return instance;
    }

    // courses ------------------------------------------------

    public boolean canShowCourse(Course course) {
        Student currentStudent = getCurrentStudent();

        return Database.getInstance().isStudentEnrolled(
                currentStudent,
                course
        );
    }

    public boolean isCoursePassed(Course course) {
        Student currentStudent = getCurrentStudent();

        if (!Database.getInstance().isStudentEnrolled(currentStudent, course)) {
            return false;
        }

        Database database = Database.getInstance();

        return isDifficultyLevelPassed(
                database.getEnrollment(currentStudent, course)
                        .getCourse()
                        .getDifficultyLevels()
                        .getLast()
        );
    }

    // difficulty levels --------------------------------------

    public boolean isDifficultyLevelUnlocked(Course course, DifficultyLevel difficultyLevel) {
        if (!shouldShowExam()) {
            return true;
        }

        Student currentStudent = getCurrentStudent();
        Database database = Database.getInstance();

        Difficulty currentDifficulty = database.getCurrentDifficulty(currentStudent, course);

        Difficulty levelDifficulty = difficultyLevel.getDifficulty();

        return levelDifficulty.ordinal() <= currentDifficulty.ordinal();
    }

    public boolean isDifficultyLevelPassed(DifficultyLevel level) {
        Student currentStudent = getCurrentStudent();

        Exam lastUnitExam = level.getUnits().getLast().getUnitExam();

        if (!shouldShowExam() || lastUnitExam == null) {
            return false;
        }

        return lastUnitExam.isCompletedByStudent(currentStudent);
    }

    // units --------------------------------------------------

    public boolean isUnitEmpty(Unit unit) {
        return unit.getSections() == null;
    }

    private record UnitProgress(
            Difficulty currentDifficulty,
            int currentUnitIndex,
            boolean hasRemainingUnits
    ) {}

    private UnitProgress getUnitProgress(Course course) {
        Student currentStudent = getCurrentStudent();
        Database database = Database.getInstance();

        int remainingUnits = currentStudent.getRemainingUnits();

        return new UnitProgress(
                database.getCurrentDifficulty(currentStudent, course),
                database.getCurrentUnitIndex(currentStudent, course),
                remainingUnits > 0 || remainingUnits == -1
        );
    }

    private boolean isUnitUnlockedByProgress(Course course, Unit unit, UnitProgress progress) {
        if (progress.currentUnitIndex() < 0) {
            return false;
        }

        int unitIndex = course.getUnitsByDifficulty(progress.currentDifficulty()).indexOf(unit);

        return unitIndex >= 0 && unitIndex <= progress.currentUnitIndex();
    }

    public boolean isUnitUnlocked(Course course, Unit unit) {
        if (!shouldShowExam()) {
            return true;
        }

        UnitProgress progress = getUnitProgress(course);
        if (progress.currentUnitIndex() < 0) {
            return false;
        }
        Difficulty currentDifficulty = progress.currentDifficulty();
        Difficulty unitDifficulty = course.getDifficultyOfUnit(unit);
        if (unitDifficulty.ordinal() < currentDifficulty.ordinal()) {
            return true;
        }
        if (unitDifficulty.ordinal() > currentDifficulty.ordinal()) {
            return false;
        }

        return isUnitUnlockedByProgress(course, unit, progress)
                && progress.hasRemainingUnits();
    }

    public boolean isUnitLockedBySubscriptionLimit(Course course, Unit unit) {
        if (!shouldShowExam()) {
            return false;
        }

        UnitProgress progress = getUnitProgress(course);
        if (progress.currentUnitIndex() < 0) {
            return false;
        }
        if (course.getDifficultyOfUnit(unit) != progress.currentDifficulty()) {
            return false;
        }

        return isUnitUnlockedByProgress(course, unit, progress)
                && !progress.hasRemainingUnits();
    }

    public String getUnitLockedReason(Course course, Unit unit) {
        if (isUnitUnlocked(course, unit)) {
            return null;
        }

        if (isUnitLockedBySubscriptionLimit(course, unit)) {
            return "Weekly unit limit reached. Buy more in the user panel.";
        }

        return "Previous unit must be passed to unlock this unit.";
    }

    public boolean isUnitPassed(Unit unit) {
        Student currentStudent = getCurrentStudent();

        Exam unitExam = unit.getUnitExam();

        if (!shouldShowExam() || unitExam == null) {
            return false;
        }

        return unitExam.isCompletedByStudent(currentStudent);
    }

    public void passUnitForStudent(Student student, Unit unit, Course course, Difficulty difficulty) {
        Database database = Database.getInstance();

        Database.EnrollmentDto enrollment = database.getEnrollment(student, course);

        if (enrollment.getDifficulty() != difficulty) {
            throw new IllegalArgumentException("The student is not enrolled in this difficulty.");
        }

        if (!unit.canStudentTakeExam(student)) {
            throw new IllegalArgumentException("The student cannot complete this unit yet.");
        }

        if (student.getSubscription().getTier().equals(SubscriptionTier.FREE)) {
            student.addCoins(COINS_PER_UNIT);
        }

        if (unit.equals(course.getUnitsByDifficulty(difficulty).getLast())) {
            Difficulty nextDifficulty = difficulty.getNextDifficulty();

            if (nextDifficulty == null) {
                student.progressUnit();
                return; // the course has been completed
            }

            enrollment.setDifficulty(nextDifficulty);
            enrollment.setCurrentUnitIndex(0);
        } else {
            enrollment.setCurrentUnitIndex(
                    enrollment.getCurrentUnitIndex() + 1
            );
        }

        student.progressUnit();
    }

    // active or inactive features ----------------------------

    public boolean shouldShowTheoreticalSection() {
        return featureController.isActive(FeatureType.THEORETICAL_SECTION);
    }

    public boolean shouldShowPracticalSection() {
        return featureController.isActive(FeatureType.PRACTICAL_SECTION);
    }

    public boolean shouldShowExam() {
        return featureController.isActive(FeatureType.EXAM);
    }

    public boolean shouldShowGroupClass() {
        return featureController.isActive(FeatureType.GROUP_CLASS);
    }

    public boolean shouldShowPrivateClass() {
        return featureController.isActive(FeatureType.PRIVATE_CLASS);
    }

    // helpers ------------------------------------------------

    private Student getCurrentStudent() {
        return AuthController.getInstance().getLoggedStudent();
    }
}
