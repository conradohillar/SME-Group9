package model.exercise;

import model.ContentType;

import java.util.List;

public class MultipleChoiceExercise extends Exercise {
    private List<ContentType> options;
    private ContentType correctAnswer;

    public MultipleChoiceExercise(String prompt, List<ContentType> options, ContentType correctAnswer) {
        super(prompt);
        if (options.isEmpty()) {
            throw new IllegalArgumentException("No options provided");
        }
        this.options = List.copyOf(options);
        if (correctAnswer == null) {
            throw new IllegalArgumentException("Correct answer must not be null.");
        }
        this.correctAnswer = correctAnswer;
    }

    public List<ContentType> getOptions() {
        return options;
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.MULTIPLE_CHOICE;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return correctAnswer.equals(userAnswer);
    }
}
