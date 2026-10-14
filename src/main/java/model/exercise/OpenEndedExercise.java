package model.exercise;

import model.ContentType;
import model.Text;

public class OpenEndedExercise extends Exercise {
    private Text correctAnswer; // mocked for now (requires exact answer)

    public OpenEndedExercise(String prompt, String correctAnswer) {
        super(prompt);
        this.correctAnswer = new Text(correctAnswer);
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.OPEN_ENDED;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.equals(userAnswer);
    }
}
