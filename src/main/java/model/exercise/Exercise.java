package model.exercise;

import model.ContentType;

import java.util.List;

// TODO: see if we would also need a factory here or not

public abstract class Exercise {
    private static int HINT_PRICE = 3; // TODO: se if we can make it increase per hint detail

    private String prompt;
    private List<String> hints; // hints are ordered ascending by difficulty
    private List<ContentType> explanations;

    public Exercise(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public abstract boolean checkAnswer(ContentType userAnswer);

}
