package model.course;

import model.exercise.Exercise;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class Exam {
    private List<Exercise> exercises;
    private final double passingScore;
    private final Duration timeLimit;

    public Exam(List<Exercise> exercises, double passingScore, Duration timeLimit) {
        this.exercises = List.copyOf(exercises);
        this.passingScore = passingScore;
        this.timeLimit = timeLimit;
    }

    public Duration getTimeLimit() {
        return timeLimit;
    }

    public List<Exercise> visibleExercises(Set<String> activeFeatures) {
        return exercises.stream()
                .filter(exercise -> activeFeatures.contains(exercise.getType().getFeatureName()))
                .toList();
    }

    // TODO: check if exam was passed (from an attempt)
    // public ExamResult evaluate(Map<Exercise, ContentType> answers, Duration elapsed,
    //                              Set<String> activeFeatures) {
}
