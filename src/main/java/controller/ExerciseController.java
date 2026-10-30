package controller;

import model.user.Student;

public class ExerciseController {
    private static ExerciseController instance;

    private ExerciseController() {
    }

    public static ExerciseController getInstance() {
        if (instance == null) {
            instance = new ExerciseController();
        }
        return instance;
    }

    public boolean shouldShowHint() {
        Student currentStudent = AuthController.getInstance().getLoggedStudent();

        boolean wantsHints = currentStudent.getUserPreferences().wantsHints();
        boolean canUseHints = currentStudent.getRemainingHints() > 0
                || currentStudent.getRemainingHints() == -1;

        return wantsHints && canUseHints;
    }

    public void useHint() {
        AuthController.getInstance().getLoggedStudent().useHint();
    }

    public boolean shouldShowExplanation() {
        Student currentStudent = AuthController.getInstance().getLoggedStudent();
        return currentStudent.getUserPreferences().wantsExplanations();
    }

}
