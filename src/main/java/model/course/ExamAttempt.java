package model.course;

import model.user.Student;

import java.time.Instant;

public class ExamAttempt {

    private final Student student;
    private final Course course;
    private final Difficulty difficulty;
    private final Unit unit;
    private final Exam exam;
    private final Instant startedAt;

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

    public double getScore() {
        return score;
    }

    public boolean isPassed() {
        return passed;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void finish(boolean passed, double score) {
        this.passed = passed;
        this.score = score;
    }
}