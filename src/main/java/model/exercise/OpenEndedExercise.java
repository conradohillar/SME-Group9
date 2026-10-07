package model.exercise;

import model.ContentType;
import model.Text;

public class OpenEndedExercise extends Exercise {
    private Text expectedAnswer; // mocked for now (requires exact answer)

    public OpenEndedExercise(String prompt, String expectedAnswer) {
        super(prompt);
        this.expectedAnswer = new Text(expectedAnswer);
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return userAnswer.equals(expectedAnswer);
    }
}
