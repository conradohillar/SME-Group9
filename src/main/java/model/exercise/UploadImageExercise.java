package model.exercise;

import model.ContentType;

public class UploadImageExercise extends Exercise {

    public UploadImageExercise(String prompt) {
        super(prompt);
    }

    @Override
    public boolean checkAnswer(ContentType userAnswer) {
        return true; // hard coded, an AI would check it
    }
}
