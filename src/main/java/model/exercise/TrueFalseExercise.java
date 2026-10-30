package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Boolean;
import model.ContentType;

public class TrueFalseExercise extends Exercise {
    private final Boolean correctAnswer;

    @JsonCreator
    public TrueFalseExercise(@JsonProperty("prompt") String prompt, @JsonProperty("correctAnswer") boolean isTrue, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        super(prompt, hint, explanation);
        this.correctAnswer = new Boolean(isTrue);
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.TRUE_FALSE;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer.getContent().toString();
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
