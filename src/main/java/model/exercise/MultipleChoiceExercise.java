package model.exercise;

import model.ContentType;

import java.util.List;
import java.util.Objects;

public class MultipleChoiceExercise extends Exercise {
    private final List<String> options;
    private final String correctAnswer;

    public MultipleChoiceExercise(String prompt, List<String> options, String correctAnswer, List<String> hints, List<String> explanations) {
        super(prompt, hints, explanations);
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("Options must not be empty.");
        }
        this.options = List.copyOf(options);
        if (!this.options.contains(correctAnswer)) {
            throw new IllegalArgumentException("Correct answer must be one of the options.");
        }
        this.correctAnswer = Objects.requireNonNull(correctAnswer);
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.MULTIPLE_CHOICE;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.equals(userAnswer.toString());
    }
}
