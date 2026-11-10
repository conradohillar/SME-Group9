package controller;

import model.exercise.Exercise;
import model.feature.FeatureType;
import model.user.Student;
import utils.datatype.ContentType;

public class ExerciseController {
    private static final ExerciseController instance = new ExerciseController();

    private ExerciseController() {
    }

    public static ExerciseController getInstance() {
        return instance;
    }

    public boolean shouldShowHint() {
        if (!FeatureController.getInstance().isActive(FeatureType.EXERCISE_HINTS)) {
            return false;
        }
        Student currentStudent = getCurrentStudent();
        boolean wantsHints = currentStudent.getStudentPreferences().wantsHints();
        boolean canUseHints = currentStudent.getRemainingHints() > 0 || currentStudent.getRemainingHints() == -1;

        return wantsHints && canUseHints;
    }

    public void useHint() {
        getCurrentStudent().useHint();
    }

    public boolean shouldShowExplanation() {
        if (!FeatureController.getInstance().isActive(FeatureType.EXERCISE_EXPLANATIONS)) {
            return false;
        }
        Student currentStudent = getCurrentStudent();
        return currentStudent.getStudentPreferences().wantsExplanations();
    }

    public boolean isAnswerCorrect(Exercise exercise, ContentType userAnswer) {
        return exercise.checkAnswer(userAnswer);
    }

    private Student getCurrentStudent() {
        return AuthController.getInstance().getLoggedStudent();
    }

}
