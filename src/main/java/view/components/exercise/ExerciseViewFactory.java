package view.components.exercise;

import model.exercise.Exercise;
import model.exercise.MultipleChoiceExercise;

public class ExerciseViewFactory {
    public ExerciseView create(Exercise exercise) {
        return switch (exercise.getType()) {
            case MULTIPLE_CHOICE ->
                    new MultipleChoiceView(
                            ((MultipleChoiceExercise) exercise).getOptions()
                    );
            default -> throw new IllegalArgumentException(
                    "Unsupported exercise type: "
                            + exercise.getType()
            );
        };
    }
}
