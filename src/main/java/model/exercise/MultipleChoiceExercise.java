package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.ContentType;

import java.util.List;
import java.util.Objects;

public class MultipleChoiceExercise extends Exercise {
    // TODO: ver porque aca String y no Text. deberia ser text
    private final List<String> options;
    private final String correctAnswer;

    @JsonCreator
    public MultipleChoiceExercise(@JsonProperty("prompt") String prompt, @JsonProperty("options") List<String> options, @JsonProperty("correctAnswer") String correctAnswer, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        super(prompt, hint, explanation);
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
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.equals(userAnswer.getContent());
    }
}
