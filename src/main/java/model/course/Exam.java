package model.course;

import model.ContentType;
import model.exercise.Exercise;
import model.user.Student;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Exam implements CompletableByStudent {
    private final List<Exercise> exercises;
    private final double passingPercentage;
    private final Duration timeLimit;

    public Exam(List<Exercise> exercises, double passingPercentage, Duration timeLimit) {
        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Exam must contain at least one exercise.");
        }
        if (passingPercentage < 0 || passingPercentage > 100) {
            throw new IllegalArgumentException("Passing percentage must be between 0 and 100.");
        }
        if (timeLimit == null || timeLimit.isNegative() || timeLimit.isZero()) {
            throw new IllegalArgumentException("Time limit must be positive.");
        }
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

     public ExamResult evaluate(Course course, Difficulty difficulty, Unit unit, Student student, Map<Exercise, ContentType> answers, Duration elapsed) {
        ExamResult result = new ExamResult(course, difficulty, unit);
        if ((elapsed.compareTo(timeLimit)) > 0) {
            result.setScore(0.0);
            result.setPassed(false);
            return result;
        }
        int points = 0;
        for (Exercise exercise : exercises) { // TODO: this should be for exercise in visible exercises (at the moment of the attempt, check how to do this)
            ContentType answer = answers.get(exercise);
            if (answer != null && exercise.checkAnswer(answer)) {
                points++;
            }
        }
         double score = points / (double) exercises.size() * 100;
         result.setScore(score);
         result.setPassed(score >= passingPercentage);
        if (result.isPassed()) {
            // TODO: unmock
           // database.approveUnitForStudent(student, unit)
        }
        return result;
     }

     public boolean isCompletedByStudent(Student student) {
        // TODO: unmock
        // database.hasUserPassed(user, this) // mocked for now
         return true;
     }

     // TODO: missing attempt exam method or something like that, where we save exercises and answers
    // check what happens with deactivated exercise types/etc after the exam is created and before it is taken

}
