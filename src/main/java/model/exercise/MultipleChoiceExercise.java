package model.exercise;

import model.ContentType;

import java.util.Set;

public class MultipleChoiceExercise extends Exercise {
    private Set<ContentType> options;
    private ContentType correctAnswer;

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return userAnswer.equals(correctAnswer);
    }
}
