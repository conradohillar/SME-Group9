package model.exercise;

import model.ContentType;

import java.util.List;

public abstract class Exercise {
    private static int HINT_PRICE = 3; // TODO: se if we can make it increase per hint detail

    private String prompt;
    private List<String> hints; // hints are ordered ascending by difficulty
    private List<ContentType> explanations;

    public abstract boolean checkAnswer(ContentType userAnswer);

    // TODO: ask whether we need to create objects like "UserExerciseAttempt" that stores the exercise state for each user
}
