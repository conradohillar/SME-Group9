package model.exercise;

import model.ContentType;
import model.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class Exercise {
    private static final int HINT_PRICE = 3;

    private final String prompt;
    private List<String> hints = new ArrayList<>(); // hints (should be) inserted ascending by difficulty
    private List<ContentType> explanations = new ArrayList<>();

    protected Exercise(String prompt, List<String> hints, List<ContentType> explanations) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("Prompt must not be blank.");
        }
        this.prompt = prompt;
        this.hints = hints;
        this.explanations = explanations;
    }

    public String getPrompt() {
        return prompt;
    }

    public abstract ExerciseType getType();

    public abstract boolean checkAnswer(ContentType userAnswer);

    public ContentType parseAnswer(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Answer must not be null.");
        }
        return new Text(input.trim());
    }

    public Exercise addHint(String hint) {
        hints.add(hint);
        return this;
    }

    public Exercise addExplanation(ContentType explanation) {
        explanations.add(explanation);
        return this;
    }

    public int getHintCount() {
        return hints.size();
    }

    public String getHint(int index) {
        return hints.get(index);
    }

    public List<ContentType> getExplanations() {
        return List.copyOf(explanations);
    }

}
