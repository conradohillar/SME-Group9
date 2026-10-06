package model.exercise;

import model.ContentType;
import model.Text;

import java.util.List;

public class CompletePhraseExercise extends Exercise {
    private final List<String> phrases; // Between strings is an option for
    private final List<String> acceptedAnswers; // some questions may have more than one valid answer

    public CompletePhraseExercise(List<String> phrases, List<String> acceptedAnswers) {
        this.acceptedAnswers = acceptedAnswers;
        this.phrases = phrases;
    }

    public boolean checkAnswer(ContentType userAnswer) {
        if (userAnswer.getClass() != Text.class){
            throw new IllegalArgumentException("Unacceptable answer type, must be Text type");
        }
        return acceptedAnswers.contains(userAnswer.getContent());
    }
}
