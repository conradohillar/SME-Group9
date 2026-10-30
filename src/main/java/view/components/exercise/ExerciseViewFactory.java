package view.components.exercise;

import model.exercise.*;

public class ExerciseViewFactory {
    public ExerciseView create(Exercise exercise) {
        return switch (exercise.getType()) {
            case MULTIPLE_CHOICE -> new MultipleChoiceView(((MultipleChoiceExercise) exercise).getOptions());
            case OPEN_ENDED -> new OpenEndedView();
            case TRUE_FALSE -> new TrueFalseView();
            case COMPLETE_PHRASE -> new CompletePhraseView(((CompletePhraseExercise) exercise).getPhrases());
            case UPLOAD_IMAGE -> new UploadImageView();
        };
    }
}
