package controller;

import database.Database;
import model.course.Course;
import model.course.Difficulty;
import model.course.DifficultyLevel;
import model.course.Unit;
import model.user.Student;

public class CourseController {
    private static CourseController instance;

    private CourseController() {
    }

    public static CourseController getInstance() {
        if (instance == null) {
            instance = new CourseController();
        }
        return instance;
    }

    public boolean canShowCourse(Course course) {
        Student currentStudent = AuthController.getInstance().getLoggedStudent();
        return Database.getInstance().isStudentEnrolled(currentStudent, course);
    }

    public boolean isCoursePassed(Course course) {
        Student currentStudent = AuthController.getInstance().getLoggedStudent();
        if (!Database.getInstance().isStudentEnrolled(currentStudent, course)) {
            return false;
        }
        Database database = Database.getInstance();
        Unit lastUnit = database.getEnrollment(currentStudent, course).getCourse().getUnitsByDifficulty(Difficulty.getLastDifficulty()).getLast();
        return isUnitPassed(lastUnit);
    }

    public boolean isDifficultyLevelUnlocked(Course course, DifficultyLevel difficultyLevel) {
        Student currentStudent = AuthController.getInstance().getLoggedStudent();
        Database database = Database.getInstance();

        Difficulty currentDifficulty = database.getCurrentDifficulty(currentStudent, course);
        Difficulty levelDifficulty = difficultyLevel.getDifficulty();

        return levelDifficulty.ordinal() <= currentDifficulty.ordinal();
    }

    public boolean isUnitUnlocked(Course course, Unit unit) {
        Student currentStudent = AuthController.getInstance().getLoggedStudent();
        Database database = Database.getInstance();

        int currentUnitIndex = database.getCurrentUnitIndex(currentStudent, course);

        if (currentUnitIndex < 0) return false;

        Difficulty currentDifficulty = database.getCurrentDifficulty(currentStudent, course);
        Difficulty unitDifficulty = course.getDifficultyOfUnit(unit);

        if (unitDifficulty.ordinal() < currentDifficulty.ordinal()) {
            return true;
        }

        if (unitDifficulty.ordinal() > currentDifficulty.ordinal()) {
            return false;
        }

        boolean unlockedByProgress = course.getUnitsByDifficulty(currentDifficulty).indexOf(unit) <= currentUnitIndex;

        boolean unlockedBySubscriptionLimit = currentStudent.getWeeklyUnitsProgressed() < currentStudent.getSubscription().getMaxUnitsPerWeek();

        return unlockedByProgress && unlockedBySubscriptionLimit;
    }

    public boolean isUnitPassed(Unit unit) {
        Student currentStudent = AuthController.getInstance().getLoggedStudent();
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

        if (unit.equals(course.getUnitsByDifficulty(difficulty).getLast())) {
            Difficulty nextDifficulty = difficulty.getNextDifficulty();

            if (nextDifficulty == null) {
                // TODO: ver que hacemos cuando ya completo el course
                return;
            }

            enrollment.setDifficulty(nextDifficulty);
            enrollment.setCurrentUnitIndex(0);
            student.progressUnit();
        } else {
            enrollment.setCurrentUnitIndex(enrollment.getCurrentUnitIndex() + 1);
        }
    }
}
