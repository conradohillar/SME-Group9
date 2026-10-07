package model.course;

import model.exercise.Exercise;

import java.time.Duration;
import java.util.List;

public class Exam {
    private List<Exercise> exercises;
    private final double passingScore;
    private final Duration timeLimit;

    public Exam(List<Exercise> exercises, double passingScore, Duration timeLimit) {
        this.exercises = exercises;
        this.passingScore = passingScore;
        this.timeLimit = timeLimit;
    }

    // TODO: check if exam was passed (from an attempt)
}
