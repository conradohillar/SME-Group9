package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.ContentType;
import model.Text;

import java.util.List;
import java.util.Objects;

public class CompletePhraseExercise extends Exercise {
    private final List<String> phrases; // Between strings is an "empty space" that should be completed by the user
    private final Text correctAnswer; // mocked for now (requires exact answer)

    @JsonCreator
    public CompletePhraseExercise(@JsonProperty("prompt") String prompt, @JsonProperty("phrases") List<String> phrases, @JsonProperty("correctAnswer") String correctAnswer, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        super(prompt, hint, explanation);
        if (phrases == null || phrases.size() != 2) {
            throw new IllegalArgumentException("Phrases must contain exactly 2 strings.");
        }
        this.phrases = List.copyOf(phrases);;
        this.correctAnswer = new Text(Objects.requireNonNull(correctAnswer));
    }

    public List<String> getPhrases() {
        return phrases;
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.COMPLETE_PHRASE;
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
