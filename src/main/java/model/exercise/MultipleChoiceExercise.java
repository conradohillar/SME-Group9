package model.exercise;

import model.ContentType;

import java.util.Set;

public class MultipleChoiceExercise extends Exercise {
    private Set<ContentType> options;
    private ContentType expectedAnswer;

    public MultipleChoiceExercise(String prompt, Set<ContentType> options, ContentType correctAnswer) {
        super(prompt);
        this.options = options;
        this.expectedAnswer = correctAnswer;
    }

    public Set<ContentType> getOptions() {
        return options;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return userAnswer.equals(expectedAnswer);
    }
}
