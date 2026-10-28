package model.course;

import model.ContentType;
import model.exercise.Exercise;
import model.user.User;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Exam {
    private final List<Exercise> exercises;
    private final double passingPercentage;
    private final Duration timeLimit;

    public Exam(List<Exercise> exercises, double passingPercentage, Duration timeLimit) {
        this.exercises = List.copyOf(exercises);
        this.passingPercentage = passingPercentage;
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

     public ExamResult evaluate(Course course, Difficulty difficulty, Unit unit, Map<Exercise, ContentType> answers, Duration elapsed) {
        ExamResult result = new ExamResult(course, difficulty, unit);
        if ((elapsed.compareTo(timeLimit)) > 0) {
            result.setScore(0.0);
            result.setPassed(false);
            return result;
        }
        int points = 0;
        for (Exercise exercise : exercises) {
            if (exercise.checkAnswer(answers.get(exercise))) {
                points++;
            }
        }
        double score = points / (double) exercises.size();
        result.setScore(score*100);
        result.setPassed(score > passingPercentage);
        if (result.isPassed()) {
            // TODO: unmock
            int idx = course.getUnitsByDifficulty(difficulty).indexOf(unit);
           // database.approveUnitForUser(user, course, unit.getDifficulty(), idx)
        }
        return result;
     }

     public boolean isPassedByUser(User user) {
        // TODO: unmock
        // database.hasUserPassed(user, this) // mocked for now
         return true;
     }

}
