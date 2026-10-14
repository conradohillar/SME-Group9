package model.exercise;

import model.ContentType;
import model.Image;

public class UploadImageExercise extends Exercise {

    public UploadImageExercise(String prompt) {
        super(prompt);
    }

    @Override
    public ExerciseType getType() {
        return ExerciseType.UPLOAD_IMAGE;
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return true; // hard coded, an AI would check it
    }

    @Override
    public ContentType parseAnswer(String input) {
        return new Image(input.trim());
    }
}
