package controller;

import database.Database;
import model.course.Course;
import model.feature.FeatureType;
import model.user.Student;
import utils.ErrorCodes;
import utils.FeatureActivationException;
import utils.Populator;
import view.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class AppController implements ControllerInterface {
    private static final AppController instance = new AppController();
    private MainFrame mainFrame;

    private final FeatureController featureController = FeatureController.getInstance();

    private AppController() {
    }

    public static AppController getInstance() {
        return instance;
    }

    public void initializeApp() {
        try {
            Populator.populateUsers("users.json");
            Populator.populateCourses("courses.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // by default, we enroll every student in every course for simplicity (for now)
        Database database = Database.getInstance();
        for (Course course : database.getCourses()) {
            for (Student student : database.getStudents()) {
                database.enrollStudent(student, course);
            }
        }

        enableUIView();
    }

    @Override
    public int activate(String[] deactivations, String[] activations){
        Set<String> conflicting = new HashSet<>(Arrays.asList(activations));
        conflicting.retainAll(Arrays.asList(deactivations));

        ErrorCodes result = conflicting.isEmpty() ? ErrorCodes.SUCCESS : ErrorCodes.CONFLICTING_REQUEST;

        for (String deactivation : deactivations) {
            if (!conflicting.contains(deactivation)) {
                result = keepFirstError(result, changeFeatureState(deactivation, featureController::deactivate));
            }
        }

        for (String activation : activations) {
            if (!conflicting.contains(activation)) {
                result = keepFirstError(result, changeFeatureState(activation, featureController::activate));
            }
        }
        refreshUIView();
        CLIController.getInstance().printFeaturesStateAsLog(getFeaturesStateAsLog());

        return result.getCode();
    }

    private ErrorCodes changeFeatureState(String featureName, Consumer<FeatureType> stateChange) {
        FeatureType featureType;
        try {
            featureType = FeatureType.valueOf(featureName);
        } catch (IllegalArgumentException e) {
            System.out.println("Feature type \"" + featureName + "\" not found.");
            return ErrorCodes.UNKNOWN_FEATURE;
        }

        try {
            stateChange.accept(featureType);
            return ErrorCodes.SUCCESS;
        } catch (FeatureActivationException e) {
            System.out.println(e.getMessage());
            return e.getErrorCode();
        }
    }

    private ErrorCodes keepFirstError(ErrorCodes current, ErrorCodes next) {
        return current == ErrorCodes.SUCCESS ? next : current;
    }

    @Override
    public boolean enableUIView() {
        if (mainFrame == null) {
            mainFrame = new MainFrame();
        }
        SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));

        return mainFrame.isDisplayable();
    }

    @Override
    public boolean disableUIView() {
        if (mainFrame == null) {
            return false;
        }
        SwingUtilities.invokeLater(() -> mainFrame.setVisible(false));

        return true;
    }

    public boolean isUIEnabled() {
        return mainFrame != null && mainFrame.isVisible();
    }

    public void refreshUIView() {
        if(isUIEnabled()) {
            SwingUtilities.invokeLater(() -> mainFrame.refreshWholeView());
        }
    }

    private List<String> getFeaturesStateAsLog() {
        return FeatureController.getInstance().getCurrentStateLog();
    }

    @Override
    public String[] getStateAsLog() {
        Database database = Database.getInstance();
        ArrayList<String> logs = new ArrayList<>();
        for (Course course : database.getCourses()) {
            logs.addAll(course.getCurrentStateLog(featureController::isActive));
        }
        for (Student student : database.getStudents()) {
            logs.addAll(student.getCurrentStateLog());
        }
        logs.addAll(FeatureController.getInstance().getCurrentStateLog());
        logs.add("uiEnabled: " + isUIEnabled());
        return logs.toArray(String[]::new);
    }
}
