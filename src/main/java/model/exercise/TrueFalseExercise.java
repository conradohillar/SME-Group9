package model.exercise;

import model.Boolean;
import model.ContentType;

public class TrueFalseExercise extends Exercise {
    private final Boolean correctAnswer;

    public TrueFalseExercise(String prompt, boolean isTrue) {
        super(prompt);
        this.correctAnswer = new Boolean(isTrue);
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.TRUE_FALSE;
    }

    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.equals(userAnswer);
    }

    @Override
    public ContentType parseAnswer(String input) {
        return new Boolean(java.lang.Boolean.parseBoolean(input.trim()));
    }
}
