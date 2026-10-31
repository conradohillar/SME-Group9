package controller;

import database.Database;
import model.ContentType;
import model.course.Course;
import model.course.Difficulty;
import model.course.Unit;
import model.course.Exam;
import database.ExamAttempt;
import model.exercise.Exercise;
import model.user.Student;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class ExamController {
    private static ExamController instance;

    private ExamController() {
    }

    public static ExamController getInstance() {
        if (instance == null) {
            instance = new ExamController();
        }
        return instance;
    }

    public ExamAttempt startExam(Course course, Difficulty difficulty, Unit unit, Exam exam) {
        Student student = getCurrentStudent();

        return new ExamAttempt(student, course, difficulty, unit, exam);
    }

    public void finishExam(ExamAttempt attempt, Map<Exercise, ContentType> exercises) {
        Instant finishedAt = Instant.now();
        Exam exam = attempt.getExam();
        double score = exam.checkAnswers(exercises);
        boolean passed = exam.isPassed(score, Duration.between(attempt.getStartedAt(), finishedAt));
        attempt.finish(finishedAt, passed, score);
        Database.getInstance().saveExamAttempt(attempt);
        if (passed) {
            CourseController.getInstance().passUnitForStudent(attempt.getStudent(), attempt.getUnit(), attempt.getCourse(), attempt.getDifficulty());
        }
    }

    public ExamAttempt getLatestAttempt(Unit unit) {
        Student student = getCurrentStudent();

        return ExamAttempt.getLatestForStudent(student, unit);
    }

    private Student getCurrentStudent() {
        return AuthController.getInstance().getLoggedStudent();
    }
}