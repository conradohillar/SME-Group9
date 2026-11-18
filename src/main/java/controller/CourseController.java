
package controller;

import database.Database;
import model.course.ExamAttempt;
import model.course.*;
import model.exercise.Exercise;
import model.feature.FeatureType;
import model.user.Student;

import java.util.List;

public class CourseController {

    private static final CourseController instance = new CourseController();
    private static final int COINS_PER_UNIT = 2;

    private static final FeatureController featureController = FeatureController.getInstance();
    private static final UserController userController = UserController.getInstance();

    private CourseController() {
    }

    public static CourseController getInstance() {
        return instance;
    }

    // courses ------------------------------------------------

    public List<Course> getCourses() {
        return Database.getInstance().getCourses();
    }

    public boolean canShowCourse(Course course) {
        Student currentStudent = getCurrentStudent();

        return Database.getInstance().isStudentEnrolled(
                currentStudent,
                course
        );
    }

    public boolean isCoursePassed(Course course) {
        if (!Database.getInstance().isStudentEnrolled(getCurrentStudent(), course)) {
            return false;
        }

        return isDifficultyLevelPassed(course.getLastDifficultyLevel());
    }

    // chatbot ------------------------------------------------

    public boolean shouldShowAiChatbot(boolean insideCourse) {
        return insideCourse && featureController.isActive(FeatureType.AI_CHATBOT) && userController.wantsAiChatbot();
    }

    // difficulty levels --------------------------------------

    public boolean isDifficultyLevelUnlocked(Course course, DifficultyLevel difficultyLevel) {
        if (!shouldShowExam()) {
            return true;
        }
        Student currentStudent = getCurrentStudent();
        Database database = Database.getInstance();
        Difficulty currentDifficulty = database.getEnrollment(currentStudent, course).getCurrentDifficulty();
        Difficulty levelDifficulty = difficultyLevel.getDifficulty();

        return levelDifficulty.ordinal() <= currentDifficulty.ordinal();
    }

    public boolean isDifficultyLevelPassed(DifficultyLevel level) {
        return isUnitPassed(level.getUnits().getLast());
    }

    // units --------------------------------------------------

    public boolean isUnitEmpty(Unit unit) {
        return getVisibleSections(unit).isEmpty();
    }

    public List<Section> getVisibleSections(Unit unit) {
        return unit.getSections().stream()
                .filter(section -> featureController.isActive(section.getFeatureType()))
                .toList();
    }

    public List<Exercise> getVisibleExercises(PracticalSection section) {
        if (!featureController.isActive(section.getFeatureType())) {
            return List.of();
        }

        return section.getExercises().stream()
                .filter(exercise -> featureController.isActive(exercise.getFeatureType()))
                .toList();
    }

    public Exam getVisibleUnitExam(Unit unit) {
        Exam unitExam = unit.getUnitExam();

        if (unitExam == null || getVisibleExercises(unitExam).isEmpty()) {
            return null;
        }

        return unitExam;
    }

    private record UnitProgress(
            Difficulty currentDifficulty,
            int currentUnitIndex,
            boolean hasRemainingUnits
    ) {}

    private UnitProgress getUnitProgress(Course course) {
        Student currentStudent = getCurrentStudent();
        Database database = Database.getInstance();
        Enrollment enrollment = database.getEnrollment(currentStudent, course);

        return new UnitProgress(
                enrollment.getCurrentDifficulty(),
                enrollment.getCurrentUnitIndex(),
                currentStudent.hasUnitsLeft()
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
        if (getVisibleUnitExam(unit) == null) {
            return false;
        }

        ExamAttempt latestAttempt = Database.getInstance().getLatestExamAttempt(getCurrentStudent(), unit);

        return latestAttempt != null && latestAttempt.isPassed();
    }

    public void passUnitForStudent(Student student, Unit unit, Course course, Difficulty difficulty) {
        Enrollment enrollment = Database.getInstance().getEnrollment(student, course);
        if (!enrollment.isAt(difficulty)) {
            throw new IllegalArgumentException("The student is not enrolled in this difficulty.");
        }
        if (!enrollment.isCurrentUnit(unit)) {
            throw new IllegalArgumentException("The previous unit must be passed before completing this one.");
        }
        ExamAttempt latestAttempt = Database.getInstance().getLatestExamAttempt(student, unit);
        if (latestAttempt != null && latestAttempt.isPassed()) {
            throw new IllegalArgumentException("This unit has already been approved.");
        }
        if (!student.hasUnitsLeft()) {
            throw new IllegalStateException("No units left this week.");
        }

        if (student.isFreeTier()) {
            student.addCoins(COINS_PER_UNIT);
        }
        enrollment.passUnit(unit);
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
