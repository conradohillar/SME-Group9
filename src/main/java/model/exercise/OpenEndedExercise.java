package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.ContentType;
import model.Text;
import model.feature.FeatureType;

import java.util.Objects;

public class OpenEndedExercise extends Exercise {
    private final Text correctAnswer; // mocked for now (requires exact answer)

    @JsonCreator
    public OpenEndedExercise(@JsonProperty("prompt") String prompt, @JsonProperty("correctAnswer") String correctAnswer, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        super(prompt, hint, explanation, FeatureType.EXERCISE_OPEN_ENDED);
        this.correctAnswer = new Text(Objects.requireNonNull(correctAnswer));
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.OPEN_ENDED;
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
