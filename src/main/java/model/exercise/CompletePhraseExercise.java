package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.ContentType;
import model.Text;
import model.feature.FeatureType;

import java.util.List;
import java.util.Objects;

public class CompletePhraseExercise extends Exercise {
    // we leave phrases as a string and not text since we consider "part" of the prompt
    private final List<String> phrases; // between the phrases we place the empty space for the user to complete
    private final Text correctAnswer; // for now, it requires the exact answer

    @JsonCreator
    public CompletePhraseExercise(@JsonProperty("prompt") String prompt, @JsonProperty("phrases") List<String> phrases, @JsonProperty("correctAnswer") String correctAnswer, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        super(prompt, hint, explanation, FeatureType.EXERCISE_COMPLETE_PHRASE);
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
        // user answer will always be of type Text in this type of exercise, so it is safe to do the toString
        return correctAnswer.getContent().equalsIgnoreCase(userAnswer.getContent().toString());
    }
}
