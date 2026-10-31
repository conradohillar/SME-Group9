package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.ContentType;
import model.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MultipleChoiceExercise extends Exercise {
    private List<Text> options;
    private final Text correctAnswer;

    @JsonCreator
    public MultipleChoiceExercise(@JsonProperty("prompt") String prompt, @JsonProperty("options") List<String> options, @JsonProperty("correctAnswer") String correctAnswer, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        super(prompt, hint, explanation);
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("Options must not be empty.");
        }
        this.options = new ArrayList<>();

        for (String option : options) {
            Objects.requireNonNull(option);
            this.options.add(new Text(option));
        }
        Objects.requireNonNull(correctAnswer);
        this.correctAnswer = new Text(correctAnswer);
        if (!this.options.contains(this.correctAnswer)) {
            throw new IllegalArgumentException("Correct answer must be one of the options.");
        }
    }

    public List<String> getOptions() {
        return options.stream()
                .map(Text::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.MULTIPLE_CHOICE;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer.getContent();
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.getContent().equals(userAnswer.getContent());
    }
}
