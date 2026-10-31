package model.exercise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.ContentType;

public class UploadImageExercise extends Exercise {

    @JsonCreator
    public UploadImageExercise(@JsonProperty("prompt") String prompt, @JsonProperty("hint") String hint, @JsonProperty("explanation") String explanation) {
        super(prompt, hint, explanation);
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.UPLOAD_IMAGE;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return true; // hard coded, an AI would check it
    }
}
