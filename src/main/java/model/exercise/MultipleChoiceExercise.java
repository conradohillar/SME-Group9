package model.exercise;

import model.ContentType;

import java.util.List;

public class MultipleChoiceExercise extends Exercise {
    private List<ContentType> options;
    private ContentType correctAnswer;

    public MultipleChoiceExercise(String prompt, List<ContentType> options, ContentType correctAnswer) {
        super(prompt);
        this.options = List.copyOf(options);
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
