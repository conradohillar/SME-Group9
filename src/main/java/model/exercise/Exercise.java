package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.ContentType;
import model.Text;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CompletePhraseExercise.class, name = "COMPLETE_PHRASE"),
        @JsonSubTypes.Type(value = MultipleChoiceExercise.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = OpenEndedExercise.class, name = "OPEN_ENDED"),
        @JsonSubTypes.Type(value = TrueFalseExercise.class, name = "TRUE_FALSE"),
        @JsonSubTypes.Type(value = UploadImageExercise.class, name = "UPLOAD_IMAGE"),
})
public abstract class Exercise {
    private final String prompt;
    private final String hint;
    private final String explanation;

    @JsonCreator
    protected Exercise(@JsonProperty("prompt") String prompt, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("Prompt must not be blank.");
        }
        this.prompt = prompt;
        this.hint = hint;
        this.explanation = explanation;
    }

    public String getPrompt() {
        return prompt;
    }

    public abstract ExerciseType getType();

    public abstract boolean checkAnswer(ContentType userAnswer);

    public String getCorrectAnswer() {
        return null;
    }

    public String getHint() {
        return hint;
    }

    public String getExplanation() {
        return explanation;
    }

}
