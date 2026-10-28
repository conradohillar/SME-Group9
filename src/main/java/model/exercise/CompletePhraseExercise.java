package model.exercise;

import model.ContentType;
import model.Text;

import java.util.List;

public class CompletePhraseExercise extends Exercise {
    private final List<String> phrases; // Between strings is an "empty space" that should be completed by the user
    private final Text correctAnswer; // mocked for now (requires exact answer)

    public CompletePhraseExercise(String prompt, List<String> phrases, String correctAnswer) {
        super(prompt);
        if (phrases.size() != 2) {
            throw new IllegalArgumentException("Phrases list must contain exactly 2 strings for a complete phrase exercise.");
        }
        if (correctAnswer == null) {
            throw new IllegalArgumentException("Correct answer must not be null.");
        }
        this.phrases = List.copyOf(phrases);;
        this.correctAnswer = new Text(correctAnswer);
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
        return correctAnswer.equals(userAnswer);
    }
}
