package model.exercise;

import model.ContentType;
import model.Text;

import java.util.ArrayList;
import java.util.List;

public class CompletePhraseExercise extends Exercise {
    private final List<String> phrases; // Between strings is an "empty space" that should be completed by the user
    private final Text acceptedAnswer; // mocked for now (requires exact answer)

    public CompletePhraseExercise(String prompt, List<String> phrases, String acceptedAnswer) {
        super(prompt);
        this.phrases = List.copyOf(phrases);;
        this.acceptedAnswer = new Text(acceptedAnswer);
    }

    public List<String> getPhrases() {
        return phrases;
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.COMPLETE_PHRASE;
    }

    public boolean checkAnswer(ContentType userAnswer) {
        return acceptedAnswer.equals(userAnswer);
    }
}
