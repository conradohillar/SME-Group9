package model.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.FeatureController;
import database.Database;
import database.ExamAttempt;
import model.ContentType;
import model.exercise.Exercise;
import model.feature.FeatureType;
import model.user.Student;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Exam extends PracticalSection {
    private Unit unit;
    private final double passingPercentage;
    @JsonIgnore
    private final Duration timeLimit;

    private final FeatureController featureController = FeatureController.getInstance();

    @JsonCreator
    public Exam(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("exercises") List<Exercise> exercises,
            @JsonProperty("passingPercentage") double passingPercentage,
            @JsonProperty("timeLimitMinutes") long timeLimitMinutes
    ) {
        super(title, description, exercises, FeatureType.EXAM);

        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Exam must contain at least one exercise.");
        }
        if (passingPercentage < 0 || passingPercentage > 100) {
            throw new IllegalArgumentException("Passing percentage must be between 0 and 100.");
        }
        if (timeLimitMinutes <= 0) {
            throw new IllegalArgumentException("Time limit must be positive.");
        }
        this.timeLimit = Duration.ofMinutes(timeLimitMinutes);
        this.passingPercentage = passingPercentage;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Duration getTimeLimit() {
        return timeLimit;
    }

    public double checkAnswers(Map<Exercise, ContentType> answers) {
        int correctAnswers = 0;
        for (Exercise exercise : exercises) {
            ContentType answer = answers.get(exercise);
            if (answer != null && exercise.checkAnswer(answer)) {
                correctAnswers++;
            }
        }
        return correctAnswers / (double) exercises.size() * 100;
    }

    public boolean isPassed(double score, Duration elapsedTime) {
        return elapsedTime.compareTo(timeLimit) <= 0 && score >= passingPercentage;
    }

    @Override
    public boolean isCompletedByStudent(Student student) {
        ExamAttempt latestAttempt = Database.getInstance().getLatestExamAttempt(student, unit);

        return latestAttempt != null && latestAttempt.isPassed();
    }

    @Override
    public List<String> getCurrentStateLog() {
        return getCurrentStateLogWithCustomName("unitExam");
    }
}