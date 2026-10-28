package model.exercise;

import model.Boolean;
import model.ContentType;

import java.util.List;

public class TrueFalseExercise extends Exercise {
    private final Boolean correctAnswer;

    public TrueFalseExercise(String prompt, boolean isTrue, List<String> hints, List<ContentType> explanations) {
        super(prompt, hints, explanations);
        this.correctAnswer = new Boolean(isTrue);
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.TRUE_FALSE;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.getContent().equals(userAnswer.getContent());
    }

    @Override
    public ContentType parseAnswer(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Answer must not be null.");
        }
        String normalized = input.trim();
        if (!normalized.equalsIgnoreCase("true")
                && !normalized.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException(
                    "Answer must be true or false."
            );
        }
        return new model.Boolean(
                java.lang.Boolean.parseBoolean(normalized)
        );
    }
}
