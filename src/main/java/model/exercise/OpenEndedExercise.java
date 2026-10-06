package model.exercise;

import model.ContentType;

public class OpenEndedExercise extends Exercise {
    private String expectedAnswer; // an AI would check it, we now mock it (requires exact answer)

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return false;
    }
}
