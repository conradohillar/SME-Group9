package database;

import model.course.Course;
import model.course.Difficulty;
import model.course.Unit;
import model.exam.Exam;
import model.user.Student;

import java.time.Duration;
import java.time.Instant;

public class ExamAttempt {

    private final Student student;
    private final Course course;
    private final Difficulty difficulty;
    private final Unit unit;
    private final Exam exam;
    private final Instant startedAt;
    private Instant finishedAt;

    private double score;
    private boolean passed = false;

    public ExamAttempt(Student student, Course course, Difficulty difficulty, Unit unit, Exam exam) {
        if (student == null || course == null || difficulty == null || unit == null || exam == null) {
            throw new IllegalArgumentException("Exam attempt data cannot be null.");
        }
        this.student = student;
        this.course = course;
        this.difficulty = difficulty;
        this.unit = unit;
        this.exam = exam;
        this.startedAt = Instant.now();
    }

    public boolean isFinished() {
        return finishedAt != null;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Unit getUnit() {
        return unit;
    }

    public Exam getExam() {
        return exam;
    }

    public static ExamAttempt getLatestForStudent(Student student, Unit unit) {
        return Database.getInstance().getLatestExamAttempt(student, unit);
    }

    public double getScore() {
        return score;
    }

    public boolean isPassed() {
        return passed;
    }

    public Duration getElapsedTime() {
        return finishedAt == null ? Duration.ZERO : Duration.between(startedAt, finishedAt);
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void finish(Instant finishedAt, boolean passed, double score) {
        this.finishedAt = finishedAt;
        this.passed = passed;
        this.score = score;
    }
}