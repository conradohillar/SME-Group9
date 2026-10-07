package model.exercise;

import model.Boolean;
import model.ContentType;

public class TrueFalseExercise extends Exercise {
    private final Boolean isTrue;

    public TrueFalseExercise(String prompt, boolean isTrue) {
        super(prompt);
        this.isTrue = new Boolean(isTrue);
    }

    public boolean checkAnswer(ContentType userAnswer) {
        return userAnswer.equals(isTrue);
    }
}
