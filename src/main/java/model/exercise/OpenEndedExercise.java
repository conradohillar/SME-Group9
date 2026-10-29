package model.exercise;

import model.ContentType;
import model.Text;

import java.util.List;
import java.util.Objects;

public class OpenEndedExercise extends Exercise {
    private final Text correctAnswer; // mocked for now (requires exact answer)

    public OpenEndedExercise(String prompt, String correctAnswer, List<String> hints, List<String> explanations) {
        super(prompt, hints, explanations);
        this.correctAnswer = new Text(Objects.requireNonNull(correctAnswer));
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.OPEN_ENDED;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.getContent().equals(userAnswer.getContent());
    }
}
