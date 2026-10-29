package model.exercise;

import model.ContentType;
import model.Text;

import java.util.List;
import java.util.Objects;

public class CompletePhraseExercise extends Exercise {
    private final List<String> phrases; // Between strings is an "empty space" that should be completed by the user
    private final Text correctAnswer; // mocked for now (requires exact answer)

    public CompletePhraseExercise(String prompt, List<String> phrases, String correctAnswer, List<String> hints, List<String> explanations) {
        super(prompt, hints, explanations);
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
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.getContent().equals(userAnswer.getContent());
    }
}
