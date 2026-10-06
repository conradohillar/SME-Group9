package model.exercise;

import model.ContentType;

public class TrueFalseExercise extends Exercise {
    private boolean isTrue;

    public boolean checkAnswer(ContentType userAnswer) {
        if (!Object.equals(userAnswer.getClass(), Boolean.class)) { // TODO: fix this
            throw new IllegalArgumentException("Unacceptable answer type, must be Boolean type");
        }
        return (Boolean) userAnswer.getContent() == isTrue;
    }
}
