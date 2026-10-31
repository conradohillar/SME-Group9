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
        Student currentStudent = getCurrentStudent();
        boolean wantsHints = currentStudent.getStudentPreferences().wantsHints();
        boolean canUseHints = currentStudent.getRemainingHints() > 0 || currentStudent.getRemainingHints() == -1;

        return wantsHints && canUseHints;
    }

    public void useHint() {
        getCurrentStudent().useHint();
    }

    public boolean shouldShowExplanation() {
        Student currentStudent = getCurrentStudent();
        return currentStudent.getStudentPreferences().wantsExplanations();
    }

    private Student getCurrentStudent() {
        return AuthController.getInstance().getLoggedStudent();
    }

}
